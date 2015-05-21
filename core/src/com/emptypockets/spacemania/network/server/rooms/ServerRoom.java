package com.emptypockets.spacemania.network.server.rooms;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.holders.ArrayListProcessor;
import com.emptypockets.spacemania.holders.CleanerProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.client.payloads.engine.ClientEngineEntityManagerSyncPayload;
import com.emptypockets.spacemania.network.client.payloads.rooms.ClientRoomMessagesPayload;
import com.emptypockets.spacemania.network.client.rooms.ClientRoom;
import com.emptypockets.spacemania.network.engine.Engine;
import com.emptypockets.spacemania.network.engine.EntityType;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.sync.EntityManagerSync;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.exceptions.TooManyPlayersException;
import com.emptypockets.spacemania.network.server.player.PlayerManager;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;
import com.emptypockets.spacemania.network.server.rooms.messages.ServerRoomChatMessage;
import com.emptypockets.spacemania.network.server.rooms.messages.ServerRoomMessage;
import com.emptypockets.spacemania.network.server.rooms.messages.ServerRoomPlayerJoinMessage;
import com.emptypockets.spacemania.network.server.rooms.messages.ServerRoomPlayerLeaveMessage;
import com.emptypockets.spacemania.network.transport.ComsType;

/**
 * Created by jenfield on 14/05/2015.
 */
public class ServerRoom implements Disposable {
	int id;
	String name;
	ServerManager manager;
	ServerPlayer host;
	ArrayListProcessor<ServerRoomMessage> messageManager;
	PlayerManager playerManager;
	ClientRoom clientRoom;
	Engine engine;
	EntityManagerSync engineEntitySyncManager;

	public ServerRoom(ServerManager manager) {
		super();
		this.manager = manager;
		engine = new Engine();
		engineEntitySyncManager = new EntityManagerSync();
		engine.getEntityManager().register(engineEntitySyncManager);
		messageManager = new ArrayListProcessor<ServerRoomMessage>();
		playerManager = new PlayerManager();
		clientRoom = new ClientRoom();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o instanceof ServerPlayer) {
			return ((ServerPlayer) o).getId() == getId();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getId();
	}

	public int getMaxPlayers() {
		return playerManager.getMaxPlayers();
	}

	public void setMaxPlayers(int maxPlayers) {
		playerManager.setMaxPlayers(maxPlayers);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isEmpty() {
		return playerManager.isEmpty();
	}

	@Override
	public void dispose() {
		playerManager.clear();
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public int getPlayerCount() {
		return playerManager.getSize();
	}

	public void updateClientRoom() {
		clientRoom.read(this);
	}

	public ClientRoom getClientRoom() {
		return clientRoom;
	}

	public void processPlayerInput(){
		playerManager.process(new SingleProcessor<ServerPlayer>() {
			@Override
			public void process(ServerPlayer player) {
				player.processInput(engine);
			}
		});
	}
	public void update() {
		processPlayerInput();
		engine.update();
		engine.getEntityManager().removeDead();
		
		engineEntitySyncManager.setTime(engine.getTime());
	}

	public synchronized void broadcast() {
		// If no messages dont broadcast
		
		if (messageManager.getSize() > 0) {
			final ClientRoomMessagesPayload roomMessagePayloads = Pools.obtain(ClientRoomMessagesPayload.class);
			roomMessagePayloads.setComsType(ComsType.TCP);
			roomMessagePayloads.setRoomId(getId());
			messageManager.process(new CleanerProcessor<ServerRoomMessage>() {
				@Override
				public boolean shouldRemove(ServerRoomMessage entity) {
					roomMessagePayloads.addMessage(entity.createClientMessage());
					Pools.free(entity);
					return true;
				}
			});
			getPlayerManager().process(new SingleProcessor<ServerPlayer>() {
				@Override
				public void process(ServerPlayer entity) {
					entity.send(roomMessagePayloads);
				}
			});
		}
		
		//Process Sync Data
		engineEntitySyncManager.broadcast(this);
		
	}

	public synchronized void joinRoom(ServerPlayer player) throws TooManyPlayersException {
		playerManager.addPlayer(player);

		// Add entity for player
		Entity entity = engine.getEntityManager().createEntity(EntityType.Player);
		entity.getState().getPos().x = 10 + entity.getState().getId() * 5;
		entity.getState().getPos().y = 10 + entity.getState().getId() * 5;
		player.setEntityId(entity.getState().getId());
		engine.getEntityManager().addEntity(entity);

		// Send message that player has joined
		ServerRoomPlayerJoinMessage message = Pools.obtain(ServerRoomPlayerJoinMessage.class);
		message.setServerPlayer(player);
		messageManager.add(message);

		
		// Send initial Sync data to the player
		final EntityManagerSync initSync = new EntityManagerSync();
		initSync.setSyncTime(true);
		initSync.setTime(engine.getTime());
		getEngine().getEntityManager().process(new SingleProcessor<Entity>() {
			@Override
			public void process(Entity entity) {
				initSync.entityAdded(entity);
			}
		});
		final ClientEngineEntityManagerSyncPayload engineSync = Pools.obtain(ClientEngineEntityManagerSyncPayload.class);
		engineSync.setSyncData(initSync);
		player.send(engineSync);
		Pools.free(engineSync);
		
		sendMessage(String.format("%s has joined the room", player.getUsername()));
	}

	public void leaveRoom(ServerPlayer player) {
		playerManager.removePlayer(player);

		ServerRoomPlayerLeaveMessage message = Pools.obtain(ServerRoomPlayerLeaveMessage.class);
		message.setServerPlayer(player);
		messageManager.add(message);

		sendMessage(String.format("%s has left the room", player.getUsername()));
	}

	public void sendMessage(String messageContent) {
		sendMessage(messageContent, null);
	}

	public void sendMessage(String messageContent, String username) {
		ServerRoomChatMessage message = Pools.obtain(ServerRoomChatMessage.class);
		message.setTimestamp(System.currentTimeMillis());
		message.setMessage(messageContent);
		message.setUsername(username);
		messageManager.add(message);
	}

	public ServerPlayer getHost() {
		return host;
	}

	public void setHost(ServerPlayer host) {
		this.host = host;
	}

	public Engine getEngine() {
		return engine;
	}

}
