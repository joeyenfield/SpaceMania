package com.emptypockets.spacemania.engine.entitysystem.components.render;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentData;

public class RenderData extends ComponentData<RenderData> {
	Affine2 baseTransform;
	Affine2 transform;
	TextureRegion region;

	public RenderData(TextureRegion region, float sizeX, float sizeY, boolean center) {
		this(new Affine2(), region);
		baseTransform.idt();
		baseTransform.scale(sizeX / region.getRegionWidth(), sizeY / region.getRegionHeight());
		if (center) {
			baseTransform.preTranslate(-sizeX / 2, -sizeY / 2);
		}
	}

	public RenderData(Affine2 transform, TextureRegion region) {
		super();
		this.baseTransform = transform;
		this.transform = new Affine2();
		this.region = region;
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

}
