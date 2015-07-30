package com.emptypockets.spacemania;

import java.util.HashMap;

import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

public class KryoTest {
	public static void main(String[] args) {
		byte[] buffer = new byte[1024 * 1024 * 10];
		Kryo kryo = new Kryo();
		
		HashMap<ComponentType, String> data = new HashMap<ComponentType, String>();
		data.put(ComponentType.DESTRUCTION, "W");
		data.put(ComponentType.COLLISSION, "H");
		data.put(ComponentType.ANGULAR_MOVEMENT, "H");
		
		
		kryo.setRegistrationRequired(true);
		kryo.register(String.class);
		kryo.register(ComponentType.class);
		kryo.register(HashMap.class);
		
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

