package com.emptypockets.spacemania.network.engine.partitioning.cell;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.engine.EntityManager;
import com.emptypockets.spacemania.network.engine.IntersectorUtils;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;

public class CellSpacePartition {
	int numX;
	int numY;

	float cellSizeX = 0;
	float cellSizeY = 0;
	float startX = 0;
	float startY = 0;

	Cell[][] data;

	public CellSpacePartition() {
		create(0, 0);
	}

	public void create(int sizeX, int sizeY) {
		numX = sizeX;
		numY = sizeY;
		data = new Cell[numX][numY];

		for (int x = 0; x < numX; x++) {
			for (int y = 0; y < numY; y++) {
				data[x][y] = new Cell();
			}
		}
	}

	public void updateCellPositions(Rectangle bounds) {
		cellSizeX = bounds.width / numX;
		cellSizeY = bounds.height / numY;
		startX = bounds.x;
		startY = bounds.y;

		for (int x = 0; x < numX; x++) {
			for (int y = 0; y < numY; y++) {
				data[x][y].getBounds().set(startX + cellSizeX * x, startY + cellSizeY * y, cellSizeX, cellSizeY);
			}
		}
	}

	public void rebuild(EntityManager entityManger) {
		for (int x = 0; x < numX; x++) {
			for (int y = 0; y < numY; y++) {
				data[x][y].clear();
			}
		}

		entityManger.process(new SingleProcessor<Entity>() {
			@Override
			public void process(Entity entity) {
				Vector2 pos = entity.getPos();

				// Top Left
				int cellMinX = getCellX(pos.x - entity.getRadius());
				int cellMinY = getCellY(pos.y - entity.getRadius());

				// Bottom Right
				int cellMaxX = getCellX(pos.x + entity.getRadius());
				int cellMaxY = getCellY(pos.y + entity.getRadius());

				for (int cellX = cellMinX; cellX <= cellMaxX; cellX++) {
					for (int cellY = cellMinY; cellY <= cellMaxY; cellY++) {
						Cell cell = data[cellX][cellY];
						if (IntersectorUtils.intersects(cell.getBounds(), entity.getPos(), entity.getRadius())) {
							data[cellX][cellY].addEntity(entity);
						}
					}
				}

			}
		});

	}

	public int getCellX(float pos) {
		return MathUtils.clamp((int) MathUtils.floor((pos - startX) / cellSizeX), 0, numX - 1);
	}

	public int getCellY(float pos) {
		return MathUtils.clamp((int) MathUtils.floor((pos - startY) / cellSizeY), 0, numY - 1);
	}

	public Cell[][] getCells() {
		return data;
	}

	public void filter(ArrayList<Entity> result, Class<?> type) {
		Iterator<Entity> entIterator = result.iterator();
		while (entIterator.hasNext()) {
			Entity ent = entIterator.next();
			if (!type.isAssignableFrom(ent.getClass())) {
				entIterator.remove();
			}
		}
	}

	public void filter(ArrayList<Entity> result, EntityType type) {
		Iterator<Entity> entIterator = result.iterator();
		while (entIterator.hasNext()) {
			Entity ent = entIterator.next();
			if (!(type == ent.getType())) {
				entIterator.remove();
			}
		}
	}

	public synchronized void getNearbyEntities(Entity entity, float distance, ArrayList<Entity> result) {
		searchNearbyEntities(entity.getPos(), entity.getRadius() + distance, result);
	}

	public synchronized void getNearbyEntities(Entity entity, float distance, ArrayList<Entity> result, Class filterClass) {
		searchNearbyEntities(entity.getPos(), entity.getRadius() + distance, result, filterClass);
	}

	public synchronized void getNearbyEntities(Entity entity, float distance, ArrayList<Entity> result, EntityType type) {
		searchNearbyEntities(entity.getPos(), entity.getRadius() + distance, result, type);
	}

	public synchronized void searchNearbyEntities(Vector2 pos, float dist, ArrayList<Entity> result, Class filterClass) {
		searchNearbyEntities(pos, dist, result);
		filter(result, filterClass);
	}

	public synchronized void searchNearbyEntities(Vector2 pos, float dist, ArrayList<Entity> result, EntityType type) {
		searchNearbyEntities(pos, dist, result);
		filter(result, type);
	}

	public synchronized void searchNearbyEntities(Vector2 pos, float dist, ArrayList<Entity> result) {

		// Top Left
		int cellMinX = getCellX(pos.x - dist);
		int cellMinY = getCellY(pos.y - dist);

		// Bottom Right
		int cellMaxX = getCellX(pos.x + dist);
		int cellMaxY = getCellY(pos.y + dist);

		for (int cellX = cellMinX; cellX <= cellMaxX; cellX++) {
			for (int cellY = cellMinY; cellY <= cellMaxY; cellY++) {
				Cell cell = data[cellX][cellY];
				if (cell.hasEntities()) {
					if (cell.containedInCircle(pos, dist)) {
						result.addAll(cell.getEntities());
					} else {
						for (Entity ent : cell.getEntities()) {
							float dist2 = (dist + ent.getRadius());
							dist2 = dist2 * dist2;
							if (ent.getPos().dst2(pos) < dist2) {
								result.add(ent);
							}
						}
					}
				}
			}
		}
	}

	public void getEntities(Rectangle viewport, ArrayList<Entity> result) {
		// Top Left
		int cellMinX = getCellX(viewport.x);
		int cellMinY = getCellY(viewport.y);

		// Bottom Right
		int cellMaxX = getCellX(viewport.x + viewport.width);
		int cellMaxY = getCellY(viewport.y + viewport.height);

		for (int cellX = cellMinX; cellX <= cellMaxX; cellX++) {
			for (int cellY = cellMinY; cellY <= cellMaxY; cellY++) {
				Cell cell = data[cellX][cellY];
				if (cell.hasEntities()) {
					if (viewport.contains(viewport)) {
						result.addAll(cell.getEntities());
					} else {
						for (Entity ent : cell.getEntities()) {
							if (IntersectorUtils.intersects(viewport, ent.getPos(), ent.getRadius())) {
								result.add(ent);
							}
						}
					}
				}
			}
		}
	}

	public synchronized Entity searchNearestEntityWhereEntityInThereFOV(Entity entity, EntityType type, float maxDistance, float fov) {
		Entity result = null;
		float minDist2 = 0;

		ArrayList<Entity> entities = new ArrayList<Entity>();
		searchNearbyEntities(entity.getPos(), maxDistance, entities);
		for (Entity ent : entities) {
			if (ent.getType() == type) {
				// Check within Distance
				// Check within FOV
				if (ent.isInsideFOV(entity, fov)) {
					float dist2 = ent.dst2(entity);
					if (result == null) {
						minDist2 = dist2;
						result = ent;
					} else {
						if (minDist2 < dist2) {
							minDist2 = dist2;
							result = ent;
						}
					}
				}
			}
		}
		return result;
	}

}
