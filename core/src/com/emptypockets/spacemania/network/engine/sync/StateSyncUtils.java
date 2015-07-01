package com.emptypockets.spacemania.network.engine.sync;

import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityState;
import com.emptypockets.spacemania.plotter.DataLogger;

public class StateSyncUtils {
	public static float MAX_POS_DELTA = 200;
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
	public static void updateState(long serverTime, EntityState serverState, long clientTime, Entity entity, boolean force) {
		long delta = (clientTime - serverTime);
		float timeDelta = -(delta) / 1000f;

		serverState.delta(timeDelta);
		// Set Velocity and Acl
		entity.getState().getVel().set(serverState.getVel());
		entity.getState().setAngVel(serverState.getAngVel());

		if (entity.getLastServerOffset().len2() > MAX_POS_DELTA_2) {
			entity.getState().getPos().set(serverState.getPos());
		} else {
			entity.getLastServerOffset().set(serverState.getPos()).sub(entity.getPos());
			DataLogger.log("ent-offset-server-x", entity.getLastServerOffset().x);
		}
		entity.getState().setAng(serverState.getAng());
	}

}
