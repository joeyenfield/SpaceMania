package com.emptypockets.spacemania;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentState;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.collission.CollissionState;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.controls.ControlState;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.destruction.DestructionState;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.AngularMovementState;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.ConstrainedRegionState;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementState;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.transform.AngularTransformState;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.transform.LinearTransformState;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

public class KryoTest {
	public static void main(String[] args) {
		byte[] buffer = new byte[1024 * 1024 * 10];
		Kryo kryo = new Kryo();
		
		HashMap<ComponentType, ComponentState> data = new HashMap<ComponentType, ComponentState>();
//		data.put(ComponentType.DESTRUCTION, new DestructionData());
//		data.put(ComponentType.COLLISSION,  new CollissionData());
		data.put(ComponentType.LINEAR_TRANSFORM, new LinearTransformState());
		data.put(ComponentType.ANGULAR_TRANSFORM, new AngularTransformState());
		data.put(ComponentType.LINEAR_MOVEMENT, new LinearMovementState());
		data.put(ComponentType.ANGULAR_MOVEMENT, new AngularMovementState());
		data.put(ComponentType.CONTROL, new ControlState());
		data.put(ComponentType.DESTRUCTION, new DestructionState());
		
		
		kryo.setRegistrationRequired(true);
		kryo.register(String.class);
		kryo.register(ComponentType.class);
		kryo.register(HashMap.class);
		kryo.register(CollissionState.class);
		kryo.register(DestructionState.class);
		kryo.register(LinearMovementState.class);
		kryo.register(AngularMovementState.class);;
		kryo.register(ControlState.class);
		kryo.register(Vector2.class);
		kryo.register(LinearTransformState.class);
		kryo.register(AngularTransformState.class);
		Output output = new Output(buffer);
		
//		EntityState data = new MovingEntityState();
		kryo.writeObject(output, data);
		output.close();
		

		print(buffer, output.position());
		System.out.println("Size : "+output.position());
	}

	public static void print(byte[] data) {
		print(data, 0, data.length);
	}

	public static void print(byte[] data, int count) {
		print(data, 0, count);
	}
	
	public static void print(byte[] data, int start, int length) {
		for (int i = start; i < start + length && i < data.length; i++) {
			System.out.println("Field ["+i+"] : "+data[i]);
		}
	}
}

