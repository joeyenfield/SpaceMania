package com.emptypockets.spacemania;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentData;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.collission.CollissionData;
import com.emptypockets.spacemania.engine.entitysystem.components.controls.ControlData;
import com.emptypockets.spacemania.engine.entitysystem.components.destruction.DestructionData;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.AngularMovementData;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.ConstrainedRegionData;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.LinearMovementData;
import com.emptypockets.spacemania.engine.entitysystem.components.transform.AngularTransformData;
import com.emptypockets.spacemania.engine.entitysystem.components.transform.LinearTransformData;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

public class KryoTest {
	public static void main(String[] args) {
		byte[] buffer = new byte[1024 * 1024 * 10];
		Kryo kryo = new Kryo();
		
		HashMap<ComponentType, ComponentData> data = new HashMap<ComponentType, ComponentData>();
//		data.put(ComponentType.DESTRUCTION, new DestructionData());
//		data.put(ComponentType.COLLISSION,  new CollissionData());
		data.put(ComponentType.LINEAR_TRANSFORM, new LinearTransformData());
		data.put(ComponentType.ANGULAR_TRANSFORM, new AngularTransformData());
		data.put(ComponentType.LINEAR_MOVEMENT, new LinearMovementData());
		data.put(ComponentType.ANGULAR_MOVEMENT, new AngularMovementData());
		data.put(ComponentType.CONTROL, new ControlData());
		data.put(ComponentType.DESTRUCTION, new DestructionData());
		
		
		kryo.setRegistrationRequired(true);
		kryo.register(String.class);
		kryo.register(ComponentType.class);
		kryo.register(HashMap.class);
		kryo.register(CollissionData.class);
		kryo.register(DestructionData.class);
		kryo.register(LinearMovementData.class);
		kryo.register(AngularMovementData.class);;
		kryo.register(ControlData.class);
		kryo.register(Vector2.class);
		kryo.register(LinearTransformData.class);
		kryo.register(AngularTransformData.class);
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

