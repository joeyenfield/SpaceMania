package com.emptypockets.spacemania.engine.spatialpartition;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.partition.PartitionComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.partition.PartitionData;
import com.emptypockets.spacemania.gui.renderer.TextRender;

public class CellsGameEntitySpatitionPartition {

	int sizeX;
	int sizeY;
	GameEngine engine;
	ArrayList<GameEntity>[][] cells;
	Vector2 tempVector2 = new Vector2();
	PartitionKey tempPartitionKey = new PartitionKey();
	Rectangle tempRect = new Rectangle();

	int currentSearchId = 0;

	public CellsGameEntitySpatitionPartition(GameEngine engine, int cellsX, int cellsY) {
		this.engine = engine;
		sizeX = cellsX;
		sizeY = cellsY;
		cells = new ArrayList[sizeX][sizeY];
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				cells[x][y] = new ArrayList<GameEntity>(20);
			}
		}
	}

	public void reset() {
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				cells[x][y].clear();
			}
		}
	}

	public void encodeRange(GameEntity entity, PartitionKey range) {
		Rectangle region = engine.universeRegion;
		tempVector2.set(entity.linearTransform.data.pos);
		float radius = ((PartitionData) entity.getComponent(ComponentType.PARTITION).data).radius;
		tempVector2.x -= region.x;
		tempVector2.y -= region.y;
		range.xS = MathUtils.clamp(MathUtils.floor(((tempVector2.x - radius) / region.width) * sizeX), 0, sizeX - 1);
		range.xE = MathUtils.clamp(MathUtils.floor(((tempVector2.x + radius) / region.width) * sizeX), 0, sizeX - 1);
		range.yS = MathUtils.clamp(MathUtils.floor(((tempVector2.y - radius) / region.height) * sizeY), 0, sizeY - 1);
		range.yE = MathUtils.clamp(MathUtils.floor(((tempVector2.y + radius) / region.height) * sizeY), 0, sizeY - 1);
	}

	public void encodeRange(Rectangle rect, PartitionKey range) {
		Rectangle region = engine.universeRegion;
		tempVector2.x = rect.x - region.x;
		tempVector2.y = rect.y - region.y;
		range.xS = MathUtils.clamp(MathUtils.floor(((tempVector2.x) / region.width) * sizeX), 0, sizeX - 1);
		range.yS = MathUtils.clamp(MathUtils.floor(((tempVector2.y) / region.height) * sizeY), 0, sizeY - 1);
		tempVector2.x += rect.width;
		tempVector2.y += rect.height;
		range.xE = MathUtils.clamp(MathUtils.floor(((tempVector2.x) / region.width) * sizeX), 0, sizeX - 1);
		range.yE = MathUtils.clamp(MathUtils.floor(((tempVector2.y) / region.height) * sizeY), 0, sizeY - 1);
	}

	public synchronized void searchAnyMask(Rectangle region, int any, ArrayList<GameEntity> results) {
		encodeRange(region, tempPartitionKey);
		int size = 0;
		currentSearchId++;
		for (int x = tempPartitionKey.xS; x <= tempPartitionKey.xE; x++) {
			for (int y = tempPartitionKey.yS; y <= tempPartitionKey.yE; y++) {
				ArrayList<GameEntity> data = cells[x][y];
				size = data.size();
				for (int i = 0; i < size; i++) {
					GameEntity ent = data.get(i);
					PartitionComponent comp = (PartitionComponent) ent.getComponent(ComponentType.PARTITION);
					if (comp.currentSearchId != currentSearchId) {
						comp.currentSearchId = currentSearchId;
						if (ent.hasAnyOfAbility(any)) {
							results.add(ent);
						}
					}
				}
			}
		}
	}

	public synchronized void searchAllMask(Rectangle region, int any, ArrayList<GameEntity> results) {
		encodeRange(region, tempPartitionKey);
		int size = 0;
		currentSearchId++;
		for (int x = tempPartitionKey.xS; x <= tempPartitionKey.xE; x++) {
			for (int y = tempPartitionKey.yS; y <= tempPartitionKey.yE; y++) {
				ArrayList<GameEntity> data = cells[x][y];
				size = data.size();
				for (int i = 0; i < size; i++) {
					GameEntity ent = data.get(i);
					PartitionComponent comp = (PartitionComponent) ent.getComponent(ComponentType.PARTITION);
					if (comp.currentSearchId != currentSearchId) {
						comp.currentSearchId = currentSearchId;
						if (ent.hasAllOfAbility(any)) {
							results.add(ent);
						}
					}
				}
			}
		}
	}

	public void renderDebug(ShapeRenderer render, TextRender text, Rectangle region) {
		encodeRange(region, tempPartitionKey);
		render.begin(ShapeType.Line);
		for (int x = tempPartitionKey.xS; x <= tempPartitionKey.xE; x++) {
			for (int y = tempPartitionKey.yS; y <= tempPartitionKey.yE; y++) {
				getRegion(x, y, tempRect);
				ArrayList<GameEntity> entities = cells[x][y];
				render.setColor(entities.size() > 0 ? Color.GREEN : Color.BLUE);
				render.rect(tempRect.x, tempRect.y, tempRect.width, tempRect.height);
			}
		}
		render.end();
	}

	public void getRegion(int x, int y, Rectangle rect) {
		float xP = x / (float) sizeX;
		float yP = y / (float) sizeY;
		rect.x = engine.universeRegion.x + engine.universeRegion.width * xP;
		rect.y = engine.universeRegion.y + engine.universeRegion.width * yP;

		rect.width = engine.universeRegion.width / sizeX;
		rect.height = engine.universeRegion.height / sizeY;

	}

	public void removeEntity(GameEntity ent) {
		encodeRange(ent, tempPartitionKey);
		removeEntity(ent, tempPartitionKey);

	}

	public void removeEntity(GameEntity entity, PartitionKey lastKey) {
		for (int x = lastKey.xS; x <= lastKey.xE; x++) {
			for (int y = lastKey.yS; y <= lastKey.yE; y++) {
				cells[x][y].remove(entity);
			}
		}
	}

	public void moveEntity(GameEntity entity, PartitionKey lastKey, PartitionKey key) {
		if (lastKey.isSet()) {
			removeEntity(entity, lastKey);
		}

		if (key.isSet()) {
			for (int x = key.xS; x <= key.xE; x++) {
				for (int y = key.yS; y <= key.yE; y++) {
					cells[x][y].add(entity);
				}
			}
		} else
			System.out.println(key);
	}
}
