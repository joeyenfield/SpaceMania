package com.emptypockets.spacemania.engine.systems.entitysystem.components.render;

import org.objenesis.strategy.BaseInstantiatorStrategy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentState;

public class RenderState extends ComponentState<RenderState> {
	Affine2 baseTransform;
	Affine2 transform;
	TextureRegion region;

	public RenderState() {
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
	public void readComponentState(RenderState result) {
	}

	@Override
	public void writeComponentState(RenderState data) {
	}

	@Override
	public boolean hasStateChanged(RenderState data) {
		return false;
	}

	@Override
	public void reset() {
		region = null;
		baseTransform.idt();
		transform.idt();
	}

}
