package com.emptypockets.spacemania.network.old.client;

import java.util.ArrayList;

import com.emptypockets.spacemania.Constants;
import com.emptypockets.spacemania.holders.ArrayListProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.old.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.old.client.player.ClientPlayerManager;
import com.emptypockets.spacemania.network.old.engine.Engine;
import com.emptypockets.spacemania.network.old.engine.entities.Entity;
import com.emptypockets.spacemania.network.old.engine.entities.bullets.BulletEntity;
import com.emptypockets.spacemania.network.old.engine.entities.players.PlayerEntity;
import com.emptypockets.spacemania.network.old.engine.grid.GridSystem;
import com.emptypockets.spacemania.network.old.engine.particles.ParticleSystem;

public class ClientEngine extends Engine {

	ParticleSystem particleSystem;
	GridSystem gridManager;

	float explosiveForce = 5f;
	int gridSizeX = Constants.GRID_SIZE_X;
	int gridSizeY = Constants.GRID_SIZE_Y;

	int maxParticles = Constants.DEFAULT_PARTICLES;
	boolean dynamicGrid = Constants.GRID_DYNAMIC;

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
		
		setDynamicGrid(Constants.GRID_DYNAMIC);

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