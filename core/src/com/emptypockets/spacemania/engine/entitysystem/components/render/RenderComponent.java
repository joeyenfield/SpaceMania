package com.emptypockets.spacemania.engine.entitysystem.components.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;

public class RenderComponent extends EntityComponent<RenderData> {

	public RenderComponent(GameEntity entity, Affine2 transform, TextureRegion region) {
		super(entity, ComponentType.RENDER, new RenderData(transform, region));
	}
	
	public RenderComponent(GameEntity entity, TextureRegion region, float sizeX, float sizeY, boolean centered) {
		super(entity, ComponentType.RENDER, new RenderData(region, sizeX, sizeY, centered));
	}

	public void update(float deltaTime) {
		Vector2 pos = entity.linearTransform.data.pos;
		float angle = entity.angularTransform.data.ang;
		data.transform.idt();
		data.transform.translate(pos);
		data.transform.rotate(angle);
		data.transform.mul(data.baseTransform);
	}
	
	public void render(SpriteBatch batch){
		batch.draw(data.region, data.region.getRegionWidth(), data.region.getRegionHeight(), data.transform);
	}

}
