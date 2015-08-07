package com.emptypockets.spacemania.network.old;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.emptypockets.spacemania.RootTest;
import com.emptypockets.spacemania.network.old.engine.entities.bullets.BulletEntity;
import com.emptypockets.spacemania.network.old.engine.entities.enemy.EnemyEntity;

public class EntityCollissionTest extends RootTest{

	@Test
	public void testBulletCollission(){
		BulletEntity bullet = new BulletEntity();
		bullet.setPos(1000, 1000);
		bullet.getVel().set(0,1).scl(bullet.getMaxVelocity());
		
		EnemyEntity enemy = new EnemyEntity();
		enemy.setPos(1000, 1100);
		enemy.getVel().set(0,0);
		
		bullet.update(1);
		enemy.update(1);
		
		assertTrue(bullet.contact(enemy));
		assertTrue(enemy.contact(bullet));
	}
}
