package com.emptypockets.spacemania.network.server.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.network.client.input.ClientInput;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.engine.Engine;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;
import com.emptypockets.spacemania.network.engine.sync.EntityManagerSync;
import com.emptypockets.spacemania.network.server.ClientConnection;
import com.emptypockets.spacemania.network.server.rooms.ServerRoom;

/**
 * Created by jenfield on 12/05/2015.
 */
public class ServerPlayer implements Disposable {
	int id;
	int ping;
	String username;
	ClientConnection clientConnection;
	ServerRoom currentRoom;
	EntityManagerSync entityManagerSync;
	
	ClientPlayer clientPlayer;
	ClientInput clientInput;
	int entityId;

	float maxEntitySpeed = 100;
	float maxBulletSpeed = 200;
	long lastShootTime = 0;
	long shootInterval = 10;

	public ServerPlayer(ClientConnection clientConnection) {
		this.clientConnection = clientConnection;
		clientPlayer = new ClientPlayer();
		clientInput = new ClientInput();
		entityManagerSync = new EntityManagerSync();
	}

	public EntityManagerSync getEntityManagerSync() {
		return entityManagerSync;
	}

	public float getMaxEntitySpeed() {
		return maxEntitySpeed;
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

	public void send(ClientPayload payload) {
		clientConnection.send(payload);
	}

	public void updateClientPlayer() {
		clientPlayer.read(this);
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

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public ClientInput getClientInput() {
		return clientInput;
	}

	public void processInput(Engine engine) {
		Entity entity = engine.getEntityManager().getEntityById(entityId);
		if (entity != null) {
			entity.getVel().set(clientInput.getMove()).scl(maxEntitySpeed);
			if (System.currentTimeMillis() - lastShootTime > shootInterval) {
				if (clientInput.getShoot().len2() > 0.1) {
					lastShootTime = System.currentTimeMillis();
					Entity bullet = engine.getEntityManager().createEntity(EntityType.Bullet);
					Vector2 dir = clientInput.getShoot().cpy().nor();
					bullet.getPos().set(entity.getPos());
					bullet.getVel().set(dir).scl(maxBulletSpeed);
					engine.getEntityManager().addEntity(bullet);
				}
			}
		}
	}
}
