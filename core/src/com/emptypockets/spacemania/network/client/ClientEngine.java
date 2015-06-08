package com.emptypockets.spacemania.network.client;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.engine.Engine;
import com.emptypockets.spacemania.network.engine.EntityManagerInterface;
import com.emptypockets.spacemania.network.engine.entities.BulletEntity;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.PlayerEntity;
import com.emptypockets.spacemania.network.engine.grid.GridSystem;
import com.emptypockets.spacemania.network.engine.grid.GridSettings;
import com.emptypockets.spacemania.network.engine.particles.ParticleSystem;
import com.emptypockets.spacemania.utils.ColorUtils;

public class ClientEngine extends Engine {

	ParticleSystem particleSystem;
	GridSystem gridManager;

	float explosiveForce = 1f;
	int gridSizeX = 100;
	int gridSizeY = 100;

	int maxParticles = 10000;
	boolean dynamicGrid = true;

	public ClientEngine() {
		super();
		particleSystem = new ParticleSystem();
		particleSystem.setMaxParticles(maxParticles);
		gridManager = new GridSystem();
		GridSettings gridSettings = new GridSettings();
		gridSettings.numX = gridSizeX;
		gridSettings.numY = gridSizeY;

		float mass = 1;
		gridSettings.inverseMass = 1 / mass;

		gridSettings.links.stiffness = .00088f ;
		gridSettings.links.damping = 0.026f;

		gridSettings.ankor.stiffness = 0.00512f;
		gridSettings.ankor.damping = 0.051f;

		gridSettings.edge.stiffness = 0.01f;
		gridSettings.edge.damping = 0.1f;

//		gridSettings.inverseMass = 1f;
//		gridSettings.links.damping = 0.06f;
//		gridSettings.links.stiffness = 0.28f;
//
//		gridSettings.edge.damping = 0.1f;
//		gridSettings.edge.stiffness = 0.1f;
//
//		gridSettings.ankor.damping = .1f;
//		gridSettings.ankor.stiffness = .1f;
		gridSettings.bounds = getRegion();

		gridManager.createGrid(gridSettings);
		getEntityManager().register(particleSystem);
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
					gridManager.applyExplosion(entity.getPos(), explosiveForce, 50);
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
	}
}
