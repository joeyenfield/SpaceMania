package com.emptypockets.spacemania.engine.entitysystem.components.movement;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentData;
import com.emptypockets.spacemania.utils.RectangeUtils;

public class ConstrainedRegionData extends ComponentData<ConstrainedRegionData> {
	public Rectangle constrainedRegion;
	public float constrainRadius = 1;
	public boolean reflect = true;

	public ConstrainedRegionData(Rectangle region) {
		constrainedRegion = region;
	}

	@Override
	public void getComponentData(ConstrainedRegionData result) {
		result.constrainedRegion.set(constrainedRegion);
		result.reflect = reflect;
		result.constrainRadius = constrainRadius;
	}

	@Override
	public void setComponentData(ConstrainedRegionData data) {
		constrainedRegion.set(data.constrainedRegion);
		reflect = data.reflect;
		constrainRadius = data.constrainRadius;
	}

	@Override
	public boolean changed(ConstrainedRegionData data) {
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

}
