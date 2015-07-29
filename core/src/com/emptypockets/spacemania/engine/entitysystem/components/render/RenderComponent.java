package com.emptypockets.spacemania.engine.entitysystem.components.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;

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
		batch.draw(data.region, data.region.getRegionWidth(), data.region.getRegionHeight(), data.transform);
	}

	@Override
	public Class<RenderData> getDataClass() {
		return RenderData.class;
	}
}
