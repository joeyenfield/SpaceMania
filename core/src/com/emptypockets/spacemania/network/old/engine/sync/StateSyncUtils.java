package com.emptypockets.spacemania.network.old.engine.sync;

import com.emptypockets.spacemania.network.old.engine.entities.Entity;
import com.emptypockets.spacemania.network.old.engine.entities.MovingEntity;
import com.emptypockets.spacemania.network.old.engine.entities.states.EntityState;
import com.emptypockets.spacemania.network.old.engine.entities.states.MovingEntityState;

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
		if (entity.getState() instanceof MovingEntityState) {
			((MovingEntity) entity).getState().getVel().set(((MovingEntityState) serverState).getVel());
			((MovingEntity) entity).getState().setAngVel(((MovingEntityState) serverState).getAngVel());
		}
		if (entity.getLastServerOffset().len2() > MAX_POS_DELTA_2 || force) {
			entity.getState().getPos().set(serverState.getPos());
		} else {
			entity.getLastServerOffset().set(serverState.getPos()).sub(entity.getPos());
		}
		entity.getState().setAng(serverState.getAng());
	}

}
