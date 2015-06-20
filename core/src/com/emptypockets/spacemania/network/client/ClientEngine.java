package com.emptypockets.spacemania.network.client;

import java.util.ArrayList;

import com.emptypockets.spacemania.holders.SingleProcessor;
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

	int maxParticles = 500;
	boolean dynamicGrid = true;
	boolean regionChanged = false;
	public ClientEngine() {
		super();
		particleSystem = new ParticleSystem();
		particleSystem.setMaxParticles(maxParticles);
		gridManager = new GridSystem();
		gridManager.setup(gridSizeX, gridSizeY, getRegion());
		getEntityManager().register(particleSystem);
	}

	@Override
	public void setRegion(float x, float y, float wide, float high) {
		super.setRegion(x, y, wide, high);
		if (gridManager != null) {
			gridManager.move(getRegion());
		}
		regionChanged = true;
	}

	public GridSystem getGridData() {
		return gridManager;
	}

	@Override
	public void updateEntities(float deltaTime) {
		super.updateEntities(deltaTime);

		// Get Players and draw pixels
		ArrayList<PlayerEntity> players = getEntityManager().filterEntities(PlayerEntity.class);

		for (PlayerEntity player : players) {
			particleSystem.drawPlayerTrail(player);
		}

		particleSystem.update(this, deltaTime);

		updateGrid(deltaTime);
	}

	public void updateGrid(float deltaTime) {
		if (!dynamicGrid) {
			return;
		}
		getEntityManager().process(new SingleProcessor<Entity>() {

			@Override
			public void process(Entity entity) {
				if (entity instanceof BulletEntity)
					gridManager.applyExplosion(entity.getPos(), explosiveForce, 80);
			}
		});
		gridManager.solve();
		gridManager.update();
	}

	public ParticleSystem getParticleSystem() {
		return particleSystem;
	}

	public boolean isDynamicGrid() {
		return dynamicGrid;
	}

	public void setDynamicGrid(boolean dynamicGrid) {
		this.dynamicGrid = dynamicGrid;
		if(dynamicGrid == false){
			gridManager.setup(2, 2, getRegion());
			gridManager.setRenderType(GridSystem.RENDER_TEXTURE);
		}
	}

	public boolean isRegionChanged() {
		return regionChanged;
	}

	public void setRegionChanged(boolean change) {
		regionChanged = change;
	}
}
