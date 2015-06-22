package com.emptypockets.spacemania.network.engine.sync;

import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.network.engine.entities.EntityState;

public class StateSyncUtils {
	public static float MAX_POS_DELTA = 400;
	public static float MAX_POS_DELTA_2 = MAX_POS_DELTA * MAX_POS_DELTA;

	public static float MAX_ANGLE_DELTA = 3;

	/**
	 * Method attemps to update the client state with the latest state from the
	 * server. It adjusts the server state to the current time and then applys.
	 * 
	 * @param serverTime
	 * @param serverState
	 * @param clientTime
	 * @param clientState
	 */
	public static void updateState(long serverTime, EntityState serverState, long clientTime, EntityState clientState) {
		long delta = (clientTime - serverTime);
		float timeDelta = (delta) / 1000f;
		EntityState tempState = Pools.obtain(EntityState.class);
		serverState.write(tempState);
		tempState.delta(timeDelta);
		// Set Velocity and Acl
		clientState.getVel().set(tempState.getVel());
		clientState.setAngVel(tempState.getAngVel());

		// Only force low level positions when the entity is off by a given
		// amount
		float posDelta = clientState.getPos().dst2(tempState.getPos());

//		System.out.println("\n");
//		System.out.println("ServerT: " + serverTime);
//		System.out.println("ClientT: " + clientTime);
//		System.out.println("Server : " + serverState);
//		System.out.println("Client : " + clientState);
//		System.out.println("Temp   : " + tempState);
//		System.out.println("DELTA  : " + Math.sqrt(posDelta));
//		System.out.println("DELTA2 : " + posDelta);

		if (posDelta > MAX_POS_DELTA_2) {
//			System.out.println("--------------------------------------HARD");
			// Hard Fix
			clientState.getPos().set(tempState.getPos());
		} else {
			//			System.out.println("--------------------------------------SOFT");
			// Soft fix
			clientState.getPos().lerp(tempState.getPos(), 0.5f);
		}
		// System.out.println("Client-: "+clientState);

		if (Math.abs(clientState.getAng() - tempState.getAng()) > MAX_ANGLE_DELTA) {
			clientState.setAng(tempState.getAng());
		}
		Pools.free(tempState);
	}
}
