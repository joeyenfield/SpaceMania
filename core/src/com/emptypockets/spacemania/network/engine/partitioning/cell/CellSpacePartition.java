package com.emptypockets.spacemania.network.engine.partitioning.cell;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.holders.ObjectProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.engine.EngineRegionListener;
import com.emptypockets.spacemania.network.engine.IntersectorUtils;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;

public class CellSpacePartition<ENT extends PartitionEntity> implements EngineRegionListener {
	int numX;
	int numY;

	float cellSizeX = 0;
	float cellSizeY = 0;
	float startX = 0;
	float startY = 0;

	Cell<ENT>[][] data;
	int lastTag = 0;

	ArrayList<ENT> tempEntities = new ArrayList<ENT>();

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

	SingleProcessor<ENT> rebuildProcessor = null;

	public synchronized void rebuild(ObjectProcessor<ENT> entityManger) {
		for (int x = 0; x < numX; x++) {
			for (int y = 0; y < numY; y++) {
				data[x][y].clear();
			}
		}
		if (rebuildProcessor == null) {
			rebuildProcessor = new SingleProcessor<ENT>() {
				@Override
				public void process(ENT entity) {
					Vector2 pos = entity.getPos();
					entity.setPartitionTag(0);
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
			};
		}
		entityManger.process(rebuildProcessor);

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

	public void filterToType(ArrayList<ENT> result, Class<?> type) {
		Iterator<ENT> entIterator = result.iterator();
		while (entIterator.hasNext()) {
			ENT ent = entIterator.next();
			if (!type.isAssignableFrom(ent.getClass())) {
				entIterator.remove();
			}
		}
	}

	public void filterToType(ArrayList<ENT> result, EntityType type) {
		Iterator<ENT> entIterator = result.iterator();
		while (entIterator.hasNext()) {
			Entity ent = (Entity) entIterator.next();
			if (!(type == ent.getType())) {
				entIterator.remove();
			}
		}
	}
	
	public void filterRemove(ArrayList<ENT> result, Class<?> type) {
		Iterator<ENT> entIterator = result.iterator();
		while (entIterator.hasNext()) {
			ENT ent = entIterator.next();
			if (type.isAssignableFrom(ent.getClass())) {
				entIterator.remove();
			}
		}
	}

	public void filterRemove(ArrayList<ENT> result, EntityType type) {
		Iterator<ENT> entIterator = result.iterator();
		while (entIterator.hasNext()) {
			Entity ent = (Entity) entIterator.next();
			if ((type == ent.getType())) {
				entIterator.remove();
			}
		}
	}

	public synchronized void getNearbyEntities(Entity entity, float distance, ArrayList<ENT> result) {
		searchNearbyEntities(entity.getPos(), entity.getRadius() + distance, result);
	}

	public synchronized void getNearbyEntities(Entity entity, float distance, ArrayList<ENT> result, Class filterClass) {
		searchNearbyEntities(entity.getPos(), (entity.getRadius() + distance), result, filterClass);
	}

	public synchronized void getNearbyEntities(Entity entity, float distance, ArrayList<ENT> result, EntityType type) {
		searchNearbyEntities(entity.getPos(), entity.getRadius() + distance, result, type);
	}

	public synchronized void searchNearbyEntities(Vector2 pos, float dist, ArrayList<ENT> result, Class filterClass) {
		searchNearbyEntities(pos, dist, result);
		filterToType(result, filterClass);
	}

	public synchronized void searchNearbyEntities(Vector2 pos, float dist, ArrayList<ENT> result, EntityType type) {
		searchNearbyEntities(pos, dist, result);
		filterToType(result, type);
	}

	public synchronized void searchNearbyEntities(Vector2 pos, float dist, ArrayList<ENT> result) {
		// Top Left
		int cellMinX = getCellX(pos.x - dist);
		int cellMinY = getCellY(pos.y - dist);

		// Bottom Right
		int cellMaxX = getCellX(pos.x + dist);
		int cellMaxY = getCellY(pos.y + dist);

		float dist2 = 0;
		Cell cell;
		ENT ent;
		boolean addAll;
		lastTag++;
		int arrayLength = 0;

		for (int cellX = cellMinX; cellX <= cellMaxX; cellX++) {
			for (int cellY = cellMinY; cellY <= cellMaxY; cellY++) {
				cell = data[cellX][cellY];
				if (cell.hasEntities()) {
					addAll = cell.containedInCircle(pos, dist);
					ArrayList data = cell.getEntities();
					arrayLength = data.size();

					ENTITY_LOOP: for (int i = 0; i < arrayLength; i++) {
						ent = (ENT) data.get(i);
						if (ent.getPartitionTag() != lastTag) {
							if (!addAll) {
								dist2 = (dist + ent.getRadius());
								dist2 = dist2 * dist2;
								if (ent.getPos().dst2(pos) > dist2) {
									continue ENTITY_LOOP;
								}
							}
							ent.setPartitionTag(lastTag);
							result.add(ent);
						}
					}
				}
			}
		}
	}

	public synchronized void getEntities(Rectangle viewport, ArrayList<ENT> result) {
		// Top Left
		int cellMinX = getCellX(viewport.x);
		int cellMinY = getCellY(viewport.y);

		// Bottom Right
		int cellMaxX = getCellX(viewport.x + viewport.width);
		int cellMaxY = getCellY(viewport.y + viewport.height);

		Cell cell;
		ENT ent;
		boolean addAll;
		lastTag++;
		int arrayLength = 0;
		for (int cellX = cellMinX; cellX <= cellMaxX; cellX++) {
			for (int cellY = cellMinY; cellY <= cellMaxY; cellY++) {
				cell = data[cellX][cellY];
				if (cell.hasEntities()) {

					// addAll = cell.containedInCircle(pos, dist);
					// ENTITY_LOOP: for (Object entObj : cell.getEntities()) {
					// ent = (ENT) entObj;
					// if (ent.getPartitionTag() != lastTag) {
					// if (!addAll) {
					// dist2 = (dist + ent.getRadius());
					// dist2 = dist2 * dist2;
					// if (ent.getPos().dst2(pos) > dist2) {
					// continue ENTITY_LOOP;
					// }
					// }
					// ent.setPartitionTag(lastTag);
					// result.add(ent);
					// }
					// }
					//

					addAll = (viewport.contains(viewport));
					ArrayList data = cell.getEntities();
					arrayLength = data.size();
					ENTITY_LOOP: for (int i = 0; i < arrayLength; i++) {
						ent = (ENT) data.get(i);
						if (ent.getPartitionTag() != lastTag) {
							if (!addAll) {
								if (!IntersectorUtils.intersects(viewport, ent.getPos(), ent.getRadius())) {
									continue ENTITY_LOOP;
								}
								ent.setPartitionTag(lastTag);
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

		searchNearbyEntities(entity.getPos(), maxDistance, tempEntities);

		int length = tempEntities.size();
		for (int i = 0; i < length; i++) {
			Entity ent = (Entity) tempEntities.get(i);
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
		tempEntities.clear();
		return result;
	}

	@Override
	public void notifyRegionChanged(Rectangle region) {
		updateCellPositions(region);
	}

	public synchronized boolean hasNearbyEntities(Entity entity, float dist, Class<?> classTypes) {
		tempEntities.clear();
		searchNearbyEntities(entity.getPos(), dist, tempEntities);
		filterToType(tempEntities, classTypes);
		int count = tempEntities.size();
		tempEntities.clear();
		return count > 0;
	}

}
