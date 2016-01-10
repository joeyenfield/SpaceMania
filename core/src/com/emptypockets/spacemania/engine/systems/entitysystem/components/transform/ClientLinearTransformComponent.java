package com.emptypockets.spacemania.engine.systems.entitysystem.components.transform;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.metrics.plotter.DataLogger;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;

public class ClientLinearTransformComponent extends LinearTransformComponent {
	float errorTolerance2 = 1000*1000;
	float fixFactor = 0.1f;
	Vector2 offset = new Vector2();
	boolean first = true;
	@Override
	public synchronized void update(float deltaTime) {
		super.update(deltaTime);
		float dX = 0;
		float dY = 0;
		if (offset.len2() > 1) {
			dX = offset.x * fixFactor;
			dY = offset.y * fixFactor;
			offset.x -= dX;
			offset.y -= dY;
			state.pos.add(dX, dY);
		}
		if(DataLogger.isEnabled()){
			DataLogger.log(entity.engine.getName()+"-ent-"+entity.entityId+"-off-x", offset.x);
			DataLogger.log(entity.engine.getName()+"-ent-"+entity.entityId+"-off-y", offset.y);
		}
	}

	@Override
	public synchronized void writeData(LinearTransformState data) {
		if(DataLogger.isEnabled()){
			DataLogger.log(entity.engine.getName()+"-ent-"+entity.entityId+"-net-pos-x", data.pos.x);
			DataLogger.log(entity.engine.getName()+"-ent-"+entity.entityId+"-net-pos-y", data.pos.y);
		}
		if (this.state.pos.dst2(data.pos) > errorTolerance2|| first) {
			super.writeData(data);
			offset.setZero();
			first=false;
		} else {
			offset.set(data.pos).sub(this.state.pos);
		}
	}

	@Override
	public void reset() {
		super.reset();
		offset.setZero();
		first = true;
	}
}
