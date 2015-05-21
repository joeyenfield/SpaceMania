package com.emptypockets.spacemania.network.engine;

import com.badlogic.gdx.utils.Pools;

public class StateSyncUtils {
	public static float MAX_POS_DELTA = 15;
	public static float MAX_POS_DELTA_2 = MAX_POS_DELTA*MAX_POS_DELTA;
	
	public static float MAX_ANGLE_DELTA = 3;
	
	/**
	 * Method attemps to update the client state with the latest state from the server. 
	 * It adjusts the server state to the current time and then applys. 
	 * @param serverTime
	 * @param serverState
	 * @param clientTime
	 * @param clientState
	 */
	public static void updateState(long serverTime, EntityState serverState, long clientTime, EntityState clientState){
		float timeDelta = (clientTime-serverTime)/1000f;
		EntityState tempState = Pools.obtain(EntityState.class);
		serverState.write(tempState);
		tempState.delta(timeDelta);
		
		//Set Velocity and Acl
		clientState.getVel().set(tempState.getVel());
		clientState.getAcl().set(tempState.getAcl());
		clientState.setAngVel(tempState.getAngVel());
		
		//Only force low level positions when the entity is off by a given amount
		if(clientState.getPos().dst2(tempState.getPos()) > MAX_POS_DELTA_2){
			System.err.println("Fixing Position"+serverState.getId());
			clientState.getPos().set(tempState.getPos());
		}
		
		if(Math.abs(clientState.getAng()-tempState.getAng()) > MAX_ANGLE_DELTA){
			clientState.setAng(tempState.getAng());
		}
		Pools.free(tempState);
	}
}
