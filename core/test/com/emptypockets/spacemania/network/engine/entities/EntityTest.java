package com.emptypockets.spacemania.network.engine.entities;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.emptypockets.spacemania.RootTest;


public class EntityTest extends RootTest{

	@Test
	public void testEntityFOV() throws Exception {
		BulletEntity b1 = new BulletEntity();
		BulletEntity b2 = new BulletEntity();
		
		b1.setPos(0, 0);
		b1.setVel(0,1);
		
		b2.setPos(0, 1);		
		assertTrue(b1.isInsideFOV(b2, 30));
		
		b2.getPos().rotate(20);
		assertTrue(b1.isInsideFOV(b2, 40));
		
		b2.getPos().rotate(5);
		assertFalse(b1.isInsideFOV(b2, 40));
		
		
		b1.setPos(100, 100);
		b1.setVel(-100,0);
		
		b2.setPos(90, 100);
		assertTrue(b1.isInsideFOV(b2, 10));		
		
		
		b1.setPos(100, 100);
		b1.setVel(100,0);
		
		b2.setPos(158, 110);
		assertTrue(b1.isInsideFOV(b2, 20));		
		
	}

}
