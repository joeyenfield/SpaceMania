package com.emptypockets.spacemania;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.engine.entities.states.EntityState;
import com.emptypockets.spacemania.network.engine.entities.states.MovingEntityState;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

public class KryoTest {
	public static void main(String[] args) {
		byte[] buffer = new byte[1024 * 1024 * 10];
		Kryo kryo = new Kryo();
		kryo.setRegistrationRequired(true);
		kryo.register(EntityState.class);
		kryo.register(MovingEntityState.class);
		kryo.register(Vector2.class);
		
		Output output = new Output(buffer);

//		EntityState data = new MovingEntityState();
		Vector2 data = new Vector2();
		kryo.writeObject(output, data);
		output.close();

		print(buffer, output.position());
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

