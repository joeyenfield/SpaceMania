package com.emptypockets.spacemania.network.engine.sync;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.engine.entities.EntityState;
import com.emptypockets.spacemania.network.engine.sync.StateSyncUtils;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Output;

public class StateSyncUtilsTest {

	@Test
	public void testUpdateState() throws Exception {
		long serverTime = 0;
		EntityState serverState = new EntityState();
		
		serverState.getPos().x =100;
		serverState.getPos().y = 200;
		
		serverState.getVel().x = 1;
		serverState.getVel().y = 2;
		
		long clientTime = 1000;
		EntityState clientState = new EntityState();
		
		StateSyncUtils.updateState(serverTime, serverState, clientTime, clientState);
		
		assertEquals(101,clientState.getPos().x, 0.1);
		assertEquals(202,clientState.getPos().y, 0.1);
		
		assertEquals(1,clientState.getVel().x, 0.1);
		assertEquals(2,clientState.getVel().y, 0.1);
	}

}
