package com.emptypockets.spacemania.engine.entitysystem.components.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.collission.CollissionComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.partition.PartitionComponent;

public class RenderComponent extends EntityComponent<RenderData> {

	public RenderComponent() {
		super(ComponentType.RENDER);
	}

	public void update(float deltaTime) {
		Vector2 pos = entity.linearTransform.data.pos;
		float angle = entity.angularTransform.data.ang;
		data.transform.idt();
		data.transform.translate(pos);
		data.transform.rotate(angle);
		data.transform.mul(data.baseTransform);
	}

	public void render(SpriteBatch batch) {
		if (entity.hasAnyOfAbility(ComponentType.DESTRUCTION.getMask())) {
			batch.setColor(Color.RED);
		} else {
			batch.setColor(Color.WHITE);
		}
		batch.draw(data.region, data.region.getRegionWidth(), data.region.getRegionHeight(), data.transform);
	}

	@Override
	public Class<RenderData> getDataClass() {
		return RenderData.class;
	}

	public void renderDebug(ShapeRenderer render) {
		if (entity.hasComponent(ComponentType.PARTITION)) {
			float radius = entity.getComponent(ComponentType.PARTITION, PartitionComponent.class).data.radius;
			render.setColor(Color.GREEN);
			render.circle(entity.linearTransform.data.pos.x, entity.linearTransform.data.pos.y, radius);
		}
		
		if (entity.hasComponent(ComponentType.COLLISSION)) {
			float radius = entity.getComponent(ComponentType.COLLISSION, CollissionComponent.class).data.collissionRadius;
			render.setColor(Color.YELLOW);
			render.circle(entity.linearTransform.data.pos.x, entity.linearTransform.data.pos.y, radius);
		}
	}
}
