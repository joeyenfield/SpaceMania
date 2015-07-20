package com.emptypockets.spacemania.network.server.rooms;

import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.Constants;
import com.emptypockets.spacemania.holders.ArrayListProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.client.payloads.engine.ClientRoomEngineRegionStatePayload;
import com.emptypockets.spacemania.network.client.payloads.engine.ServerRoomDataSendProcessor;
import com.emptypockets.spacemania.network.client.payloads.rooms.ClientRoomMessagesPayload;
import com.emptypockets.spacemania.network.client.rooms.ClientRoom;
import com.emptypockets.spacemania.network.engine.EngineRegionSync;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;
import com.emptypockets.spacemania.network.engine.entities.building.BuildingEntity;
import com.emptypockets.spacemania.network.engine.entities.players.PlayerEntity;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.engine.ServerEngine;
import com.emptypockets.spacemania.network.server.exceptions.TooManyPlayersException;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;
import com.emptypockets.spacemania.network.server.player.ServerPlayerManager;
import com.emptypockets.spacemania.network.server.rooms.messages.ServerRoomChatMessage;
import com.emptypockets.spacemania.network.server.rooms.messages.ServerRoomMessage;
import com.emptypockets.spacemania.network.server.rooms.messages.ServerRoomPlayerJoinMessage;
import com.emptypockets.spacemania.network.server.rooms.messages.ServerRoomPlayerLeaveMessage;
import com.emptypockets.spacemania.utils.PoolsManager;

/**
 * Created by jenfield on 14/05/2015.
 */
public class ServerRoom implements Disposable {
	int id;
	String name;

	ServerManager manager;
	ServerPlayer host;

	ArrayListProcessor<ServerRoomMessage> messageManager;
	ClientRoom clientRoom;
	ServerEngine engine;
	ServerPlayerManager serverRoomPlayers;

	boolean hasEngineRegionStateChanged = false;
	EngineRegionSync engineRegionState;
	ServerRoomDataSendProcessor dataSendProcessor;
	long lastBroadcastTime = 0;

	public ServerRoom(ServerManager manager) {
		super();
		this.manager = manager;
		messageManager = new ArrayListProcessor<ServerRoomMessage>();
		clientRoom = new ClientRoom();
		serverRoomPlayers = new ServerPlayerManager();
		engine = new ServerEngine(serverRoomPlayers);

		engineRegionState = new EngineRegionSync();
		dataSendProcessor = new ServerRoomDataSendProcessor();
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
		return serverRoomPlayers.getMaxPlayers();
	}

	public void setMaxPlayers(int maxPlayers) {
		serverRoomPlayers.setMaxPlayers(maxPlayers);
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
		return serverRoomPlayers.isEmpty();
	}

	@Override
	public void dispose() {
		serverRoomPlayers.clear();
	}

	public ServerPlayerManager getPlayerManager() {
		return serverRoomPlayers;
	}

	public int getPlayerCount() {
		return serverRoomPlayers.getSize();
	}

	public ClientRoom getClientRoom() {
		return clientRoom;
	}

	private void updatePlayers() {
		serverRoomPlayers.process(new SingleProcessor<ServerPlayer>() {
			@Override
			public void process(ServerPlayer player) {
				player.update(engine);
			}
		});
	}

	public synchronized void update() {
		updatePlayers();
		engine.update();

		if (shouldBroadcast()) {
			broadcast();
		}

	}

	public synchronized void broadcast() {
		clientRoom.read(this);
		lastBroadcastTime = System.currentTimeMillis();
		// If no messages dont broadcast
		if (messageManager.getSize() > 0) {
			ClientRoomMessagesPayload roomMessagePayloads = PoolsManager.obtain(ClientRoomMessagesPayload.class);
			roomMessagePayloads.setRoomId(getId());
			messageManager.process(roomMessagePayloads);
			messageManager.freeToPool();
			dataSendProcessor.setRoomMessages(roomMessagePayloads);
		}

		// Engine Room Size State Broadcast
		if (hasEngineRegionStateChanged) {
			hasEngineRegionStateChanged = false;
			engineRegionState.readFrom(engine);
			ClientRoomEngineRegionStatePayload engineRegionPayload = PoolsManager.obtain(ClientRoomEngineRegionStatePayload.class);
			engineRegionPayload.setState(engineRegionState);
			dataSendProcessor.setRegionState(engineRegionPayload);
		}

		serverRoomPlayers.process(dataSendProcessor);

		// Clear and release entities that aren't needed anymore
		dataSendProcessor.clearAfterSend();
	}

	public synchronized void spawnPlayer(ServerPlayer serverPlayer) {

		PlayerEntity player = serverPlayer.respawn(engine);

		// build(serverPlayer, player, EntityType.Base);
	}

	public void build(ServerPlayer serverPlayer, PlayerEntity player, EntityType type) {
		BuildingEntity building = (BuildingEntity) engine.getEntityManager().createEntity(type);
		building.setPos(player.getPos().x, player.getPos().y + 100);
		engine.getEntityManager().addEntity(building);
	}

	public synchronized void joinRoom(ServerPlayer player) throws TooManyPlayersException {
		serverRoomPlayers.addPlayer(player);

		// Send message that player has joined
		ServerRoomPlayerJoinMessage message = PoolsManager.obtain(ServerRoomPlayerJoinMessage.class);
		message.setServerPlayer(player);
		messageManager.add(message);
		sendMessage(String.format("%s has joined the room", player.getUsername()));

		engine.getEntityManager().register(player.getEntityManagerSync());
		player.getEntityManagerSync().setTime(engine.getEngineLastUpdateTime());
	}

	public void leaveRoom(ServerPlayer player) {
		serverRoomPlayers.removePlayer(player);

		ServerRoomPlayerLeaveMessage message = PoolsManager.obtain(ServerRoomPlayerLeaveMessage.class);
		message.setServerPlayer(player);
		messageManager.add(message);

		engine.getEntityManager().removeEntityById(player.getEntityId(), true);
		engine.getEntityManager().unregister(player.getEntityManagerSync());
		player.getEntityManagerSync().reset();

		sendMessage(String.format("%s has left the room", player.getUsername()));
	}

	public void sendMessage(String messageContent) {
		sendMessage(messageContent, null);
	}

	public void sendMessage(String messageContent, String username) {
		ServerRoomChatMessage message = PoolsManager.obtain(ServerRoomChatMessage.class);
		message.setTimestamp(System.currentTimeMillis());
		message.setMessage(messageContent);
		message.setUsername(username);
		messageManager.add(message);
	}

	public void resizeRoom(float size) {
		engine.setRegion(size);
		hasEngineRegionStateChanged = true;
	}

	public ServerPlayer getHost() {
		return host;
	}

	public void setHost(ServerPlayer host) {
		this.host = host;
	}

	public ServerEngine getEngine() {
		return engine;
	}

	public long getLastBroadcastTime() {
		return lastBroadcastTime;
	}

	public boolean shouldBroadcast() {
		return System.currentTimeMillis() - lastBroadcastTime > Constants.SERVER_TIME_ROOM_BROADCAST_PEROID;
	}

}
