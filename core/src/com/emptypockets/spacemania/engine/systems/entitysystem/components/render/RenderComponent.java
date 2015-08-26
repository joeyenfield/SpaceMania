package com.emptypockets.spacemania.engine.systems.entitysystem.components.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.collission.CollissionComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.destruction.DestructionComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.partition.PartitionComponent;
import com.emptypockets.spacemania.gui.tools.TextRender;
import com.emptypockets.spacemania.utils.BitUtilities;

public class RenderComponent extends EntityComponent<RenderData> {

	public RenderComponent() {
		super(ComponentType.RENDER);
	}

	public void update(Vector2 offset) {
		Vector2 pos = entity.linearTransform.data.pos;
		float angle = entity.angularTransform.data.ang;
		data.transform.idt();
		data.transform.translate(pos);
		if (offset != null) {
			data.transform.translate(offset);
		}
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

	public void renderDebug(ShapeRenderer render, TextRender textRender, Rectangle screenView, Vector2 offset) {
		float radius = 1;
		if (entity.hasComponent(ComponentType.PARTITION)) {
			radius = entity.getComponent(ComponentType.PARTITION, PartitionComponent.class).data.radius;
			render.setColor(Color.GREEN);
			render.circle(entity.linearTransform.data.pos.x + offset.x, entity.linearTransform.data.pos.y + offset.y, radius);
		}

		if (entity.hasComponent(ComponentType.COLLISSION)) {
			radius = entity.getComponent(ComponentType.COLLISSION, CollissionComponent.class).data.collissionRadius;
			if (entity.hasAnyOfAbility(ComponentType.DESTRUCTION.getMask())) {
				render.setColor(Color.RED);
			} else {
				render.setColor(Color.YELLOW);
			}
			render.circle(entity.linearTransform.data.pos.x + offset.x, entity.linearTransform.data.pos.y + offset.y, radius);
		}
		
		textRender.render(render, (entity.getMaskString()), entity.linearTransform.data.pos.x + offset.x, entity.linearTransform.data.pos.y + offset.y+radius, 1f, screenView);
	}
}
