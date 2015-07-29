package com.emptypockets.spacemania.engine.entitysystem.components.render;

import org.objenesis.strategy.BaseInstantiatorStrategy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentData;

public class RenderData extends ComponentData<RenderData> {
	Affine2 baseTransform;
	Affine2 transform;
	TextureRegion region;

	public RenderData() {
		this.transform = new Affine2();
		this.baseTransform = new Affine2();
	}

	public void setData(TextureRegion region, float sizeX, float sizeY, boolean center) {
		this.region = region;
		baseTransform.idt();
		baseTransform.scale(sizeX / region.getRegionWidth(), sizeY / region.getRegionHeight());
		if (center) {
			baseTransform.preTranslate(-sizeX / 2, -sizeY / 2);
		}
	}


	@Override
	public void getComponentData(RenderData result) {
	}

	@Override
	public void setComponentData(RenderData data) {
	}

	@Override
	public boolean changed(RenderData data) {
		return false;
	}

	@Override
	public void reset() {
		region = null;
		baseTransform.idt();
		transform.idt();
	}

}
