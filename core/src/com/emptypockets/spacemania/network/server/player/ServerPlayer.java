package com.emptypockets.spacemania.network.server.player;

import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.network.client.input.ClientInput;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.client.player.MyPlayer;
import com.emptypockets.spacemania.network.engine.entities.EnemyEntity;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;
import com.emptypockets.spacemania.network.engine.entities.PlayerEntity;
import com.emptypockets.spacemania.network.engine.entities.collect.CollectableEntity;
import com.emptypockets.spacemania.network.engine.entities.collect.ScoreEntity;
import com.emptypockets.spacemania.network.engine.entities.wepon.BasicWeapon;
import com.emptypockets.spacemania.network.engine.entities.wepon.SpreadWeapon;
import com.emptypockets.spacemania.network.engine.entities.wepon.Weapon;
import com.emptypockets.spacemania.network.engine.sync.EntityManagerSync;
import com.emptypockets.spacemania.network.engine.sync.PlayerManagerSync;
import com.emptypockets.spacemania.network.server.ClientConnection;
import com.emptypockets.spacemania.network.server.engine.ServerEngine;
import com.emptypockets.spacemania.network.server.rooms.ServerRoom;
import com.emptypockets.spacemania.network.transport.ComsType;

/**
 * Created by jenfield on 12/05/2015.
 */
public class ServerPlayer implements Disposable {
	public static final int NO_ENTITY = -1;
	int id;
	int ping;

	int deathsCount;

	String username;
	ClientConnection clientConnection;
	ServerRoom currentRoom;
	EntityManagerSync entityManagerSync;
	Weapon weapon;

	float scrapCount;
	MyPlayer myPlayer;
	ClientPlayer clientPlayer;

	ClientInput clientInput;
	int entityId = NO_ENTITY;

	PlayerManagerSync playerManagerSync;
	private long lastPlayersBroadcast = 0;

	float magnetDistance = 100;

	public ServerPlayer(ClientConnection clientConnection) {
		this.clientConnection = clientConnection;
		clientPlayer = new ClientPlayer();
		myPlayer = new MyPlayer();
		clientInput = new ClientInput();

		entityManagerSync = new EntityManagerSync();
		playerManagerSync = new PlayerManagerSync();

		weapon = new SpreadWeapon();
		weapon = new BasicWeapon();
		scrapCount = 0;
		deathsCount = 0;
	}

	public void update(ServerEngine engine) {
		PlayerEntity entity = (PlayerEntity) engine.getEntityManager().getEntityById(entityId);
		if (entity != null) {
			// Process Movement
			entity.applyClientInput(clientInput);
			// Processing Shooting
			weapon.shoot(this, entity, engine);
		} else {

		}
	}

	public void attacked(EnemyEntity enemy, PlayerEntity playerEnt) {
		playerEnt.setAlive(false);
		playerEnt.setExplodes(true);
		
		entityId = NO_ENTITY;
		deathsCount++;
	}

	public void collect(CollectableEntity collect, PlayerEntity playerEnt) {
		collect.collect(this);
	}

	public void respawn(ServerEngine engine) {
		// Add entity for player
		Entity entity = engine.getEntityManager().createEntity(EntityType.Player);
		entity.getState().getPos().x = 0;
		entity.getState().getPos().y = 0;
		engine.moveToDistantRegionWithoutEntities(100, entity, EnemyEntity.class);
		engine.getEntityManager().addEntity(entity);
		entityId = (entity.getState().getId());
	}

	public EntityManagerSync getEntityManagerSync() {
		return entityManagerSync;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Username[");
		result.append(username);
		result.append("] Ping [");
		result.append(ping);
		result.append("]");
		result.append("Room [");
		if (currentRoom == null) {
			result.append("None");
		} else {
			result.append(currentRoom.getName());
		}
		result.append("]");
		return result.toString();
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPing() {
		return ping;
	}

	public void setPing(int ping) {
		this.ping = ping;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isInRoom() {
		return currentRoom != null;
	}

	public void updateReturnTripTime() {
		clientConnection.updateReturnTripTime();
	}

	public void processIncommingPayloads() {
		clientConnection.processIncommingPayloads();
	}

	public void send(ClientPayload payload, ComsType type) {
		clientConnection.send(payload, type);
	}

	public ClientPlayer getClientPlayer() {
		return clientPlayer;
	}

	public ServerRoom getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(ServerRoom currentRoom) {
		this.currentRoom = currentRoom;
	}

	@Override
	public void dispose() {

	}

	public ClientConnection getClientConnection() {
		return clientConnection;
	}

	public int getEntityId() {
		return entityId;
	}

	public ClientInput getClientInput() {
		return clientInput;
	}

	public void updateClientPlayerData() {
		clientPlayer.read(this);
	}

	public void updateMyPlayerData() {
		myPlayer.read(this);
	}

	public void addScrapCount(ScoreEntity scoreEntity) {
		scrapCount++;
	}

	public long getLastPlayersBroadcast() {
		return lastPlayersBroadcast;
	}

	public void setLastPlayersBroadcast(long lastPlayersBroadcast) {
		this.lastPlayersBroadcast = lastPlayersBroadcast;
	}

	public PlayerManagerSync getPlayerManagerSync() {
		return playerManagerSync;
	}

	public MyPlayer getMyPlayer() {
		return myPlayer;
	}

	public float getScrapCount() {
		return scrapCount;
	}

	public float getMagnetDistance() {
		return magnetDistance;
	}

	public void setMagnetDistance(float magnetDistance) {
		this.magnetDistance = magnetDistance;
	}

	public int getDeathsCount() {
		return deathsCount;
	}

	public void setDeathsCount(int deathsCount) {
		this.deathsCount = deathsCount;
	}
}
