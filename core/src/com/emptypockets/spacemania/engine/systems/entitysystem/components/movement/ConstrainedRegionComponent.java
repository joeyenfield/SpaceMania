package com.emptypockets.spacemania.engine.systems.entitysystem.components.movement;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;

public class ConstrainedRegionComponent extends EntityComponent<ConstrainedRegionData> {
	static float MIN_DELTA = 0.01f;

	public ConstrainedRegionComponent() {
		super(ComponentType.CONSTRAINED_MOVEMENT);
	}

	public void update(float deltaTime) {
		if (data == null) {
			return;
		}
		Vector2 pos = entity.linearTransform.data.pos;
		
		if(!entity.hasComponent(ComponentType.LINEAR_MOVEMENT)){
			return;
		}
		Vector2 vel = ((LinearMovementData) entity.getComponent(ComponentType.LINEAR_MOVEMENT).data).vel;
		float rad = data.constrainRadius;
		float inset = rad;
		Rectangle region = data.constrainedRegion;
		boolean hitWall = false;
		if (pos.x - rad < region.x) {
			pos.set(region.x + inset, pos.y);
			if (data.reflect) {
				vel.x *= -1;
			} else {
				vel.x = 0;
			}
			hitWall = true;
		}
		if (pos.x + rad > region.x + region.width) {
			pos.set(region.x + region.width - inset, pos.y);
			if (data.reflect) {
				vel.x *= -1;
			} else {
				vel.x = 0;
			}
			hitWall = true;
		}
		if (pos.y - rad < region.y) {
			pos.set(pos.x, region.y + inset);
			if (data.reflect) {
				vel.y *= -1;
			} else {
				vel.y = 0;
			}
			hitWall = true;
		}
		if (pos.y + rad > region.y + region.height) {
			pos.set(pos.x, region.y + region.height - inset);
			if (data.reflect) {
				vel.y *= -1;
			} else {
				vel.y = 0;
			}
			hitWall = true;
		}
	}

	@Override
	public Class<ConstrainedRegionData> getDataClass() {
		return ConstrainedRegionData.class;
	}
}
