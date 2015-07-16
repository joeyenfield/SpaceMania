package com.emptypockets.spacemania.network.client;

import java.util.ArrayList;

import com.emptypockets.spacemania.Constants;
import com.emptypockets.spacemania.holders.ArrayListProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.client.player.ClientPlayerManager;
import com.emptypockets.spacemania.network.engine.Engine;
import com.emptypockets.spacemania.network.engine.entities.BulletEntity;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.PlayerEntity;
import com.emptypockets.spacemania.network.engine.grid.GridSystem;
import com.emptypockets.spacemania.network.engine.particles.ParticleSystem;

public class ClientEngine extends Engine {

	ParticleSystem particleSystem;
	GridSystem gridManager;

	float explosiveForce = 5f;
	int gridSizeX = 30;
	int gridSizeY = 30;

	int maxParticles = Constants.DEFAULT_PARTICLES;
	boolean dynamicGrid = true;

	long lastServerUpdateTime = 0;

	ArrayList<PlayerEntity> tempPlayerEntityHolder;

	SingleProcessor<Entity> updateGridProcessor = null;

	ClientPlayerManager playerData;

	public ClientEngine() {
		super();
		playerData = new ClientPlayerManager();
		tempPlayerEntityHolder = new ArrayList<PlayerEntity>();
		particleSystem = new ParticleSystem(this);
		particleSystem.setMaxParticles(maxParticles);

		gridManager = new GridSystem();
		gridManager.setup(gridSizeX, gridSizeY, getRegion());

		getEntityManager().register(particleSystem);
		addRegionListener(gridManager);
		addRegionListener(particleSystem.getPartition());

	}

	@Override
	public void setRegion(float x, float y, float wide, float high) {
		super.setRegion(x, y, wide, high);
		if (gridManager != null) {
			gridManager.move(getRegion());
		}
	}

	public GridSystem getGridData() {
		return gridManager;
	}

	@Override
	public void updateEntities(float deltaTime) {
		super.updateEntities(deltaTime);

		// Get Players and draw pixels
		tempPlayerEntityHolder.clear();
		getEntityManager().filterEntities(PlayerEntity.class, tempPlayerEntityHolder);

		int count = tempPlayerEntityHolder.size();
		for (int i = 0; i < count; i++) {
			particleSystem.drawPlayerTrail(this, tempPlayerEntityHolder.get(i));
		}
		tempPlayerEntityHolder.clear();

		particleSystem.update();

		updateGrid(deltaTime);
	}

	public void updateGrid(float deltaTime) {
		if (!dynamicGrid) {
			return;
		}

		if (updateGridProcessor == null) {
			updateGridProcessor = new SingleProcessor<Entity>() {

				@Override
				public void process(Entity entity) {
					if (entity instanceof BulletEntity)
						gridManager.applyExplosion(entity.getPos(), explosiveForce, 30);
				}
			};
		}
		getEntityManager().process(updateGridProcessor);
		gridManager.solve();
		gridManager.update();
	}

	@Override
	public void update() {
		super.update();
		logEntity("client");
	}

	public ParticleSystem getParticleSystem() {
		return particleSystem;
	}

	public boolean isDynamicGrid() {
		return dynamicGrid;
	}

	public void setDynamicGrid(boolean dynamicGrid) {
		this.dynamicGrid = dynamicGrid;
		if (dynamicGrid == false) {
			gridManager.setup(2, 2, getRegion());
			gridManager.setRenderType(GridSystem.RENDER_PATH);
		}
	}

	public long getLastServerUpdateTime() {
		return lastServerUpdateTime;
	}

	public void setLastServerUpdateTime(long lastServerUpdateTime) {
		this.lastServerUpdateTime = lastServerUpdateTime;
	}

	public ClientPlayerManager getPlayerData() {
		return playerData;
	}

}