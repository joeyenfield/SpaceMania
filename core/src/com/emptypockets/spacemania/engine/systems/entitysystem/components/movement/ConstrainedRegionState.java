package com.emptypockets.spacemania.engine.systems.entitysystem.components.movement;

import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentState;
import com.emptypockets.spacemania.utils.RectangeUtils;

public class ConstrainedRegionState extends ComponentState<ConstrainedRegionState> {
	public Rectangle constrainedRegion;
	public float constrainRadius = 1;
	public boolean reflect = true;

	public ConstrainedRegionState() {
	}

	@Override
	public void readComponentState(ConstrainedRegionState result) {
		result.constrainedRegion.set(constrainedRegion);
		result.reflect = reflect;
		result.constrainRadius = constrainRadius;
	}

	@Override
	public void writeComponentState(ConstrainedRegionState data) {
		constrainedRegion.set(data.constrainedRegion);
		reflect = data.reflect;
		constrainRadius = data.constrainRadius;
	}

	@Override
	public boolean hasStateChanged(ConstrainedRegionState data) {
		if (!RectangeUtils.same(constrainedRegion, data.constrainedRegion, 0.01f)) {
			return true;
		}
		if (reflect != data.reflect) {
			return true;
		}
		if (constrainRadius != data.constrainRadius) {
			return true;
		}
		return false;
	}

	@Override
	public void reset() {
		constrainedRegion = null;
		constrainRadius = 1;
		reflect = true;
	}

	public void setConstrainedRegion(Rectangle constrainedRegion) {
		this.constrainedRegion = constrainedRegion;
	}

}
