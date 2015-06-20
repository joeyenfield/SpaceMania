package com.emptypockets.spacemania.network.engine.partitioning.cell;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.engine.entities.Entity;

public class Cell {
	Rectangle bounds;
	ArrayList<Entity> entities;

	public Cell() {
		bounds = new Rectangle();
		entities = new ArrayList<Entity>();
	}

	public void clear() {
		entities.clear();
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public ArrayList<? extends Entity> getEntities() {
		return entities;
	}

	public boolean containedInCircle(Vector2 pos, float dist) {
		if (pos.x - dist > bounds.x && pos.x + dist < bounds.x + bounds.width) {
			if (pos.y - dist > bounds.y && pos.y + dist < bounds.y + bounds.height) {
				return true;
			}
		}
		return false;
	}

	public boolean hasEntities() {
		return entities.size() > 0;
	}

}
