package com.emptypockets.spacemania.engine.spatialpartition;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.partition.PartitionData;
import com.emptypockets.spacemania.holders.ArrayListProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class CellsGameEntitySpatitionPartition extends GameEntitySpatialPartition implements SingleProcessor<GameEntity> {

	int sizeX;
	int sizeY;
	Rectangle region = new Rectangle();
	ArrayList<GameEntity>[][] cells;
	Vector2 tempVector2 = new Vector2();
	CellRange tempCellRange = new CellRange();

	public CellsGameEntitySpatitionPartition(Rectangle universe, int cellsX, int cellsY) {
		region.set(universe);
		sizeX = cellsX;
		sizeY = cellsY;
		cells = new ArrayList[sizeX][sizeY];
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				cells[x][y] = new ArrayList<GameEntity>(20);
			}
		}
	}

	@Override
	public void reset() {
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				cells[x][y].clear();
			}
		}
	}

	public void encodeRange(GameEntity entity, CellRange range) {
		tempVector2.set(entity.linearTransform.data.pos);
		float radius = ((PartitionData) entity.getComponent(ComponentType.PARTITION).data).radius;
		tempVector2.x -= region.x;
		tempVector2.y -= region.y;
		range.xS = MathUtils.clamp(MathUtils.floor(((tempVector2.x - radius) / region.width) * sizeX), 0, sizeX);
		range.xE = MathUtils.clamp(MathUtils.floor(((tempVector2.x + radius) / region.width) * sizeX), 0, sizeX);
		range.yS = MathUtils.clamp(MathUtils.floor(((tempVector2.y - radius) / region.height) * sizeY), 0, sizeY);
		range.yE = MathUtils.clamp(MathUtils.floor(((tempVector2.y + radius) / region.height) * sizeY), 0, sizeY);
	}

	@Override
	public void search(Rectangle region, ArrayListProcessor<GameEntity> results) {

	}

	@Override
	public void process(GameEntity entity) {
		encodeRange(entity, tempCellRange);
		for (int x = tempCellRange.xS; x < tempCellRange.xE; x++) {
			for (int y = tempCellRange.yS; y < tempCellRange.yE; y++) {
				cells[x][y].add(entity);
			}
		}
	}

}

class CellRange {
	int xS = 0;
	int xE = 0;
	int yS = 0;
	int yE = 0;
}
