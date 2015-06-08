package com.emptypockets.spacemania.network.engine.grid.spring;

import com.badlogic.gdx.math.MathUtils;

public class NodeLinkSettings{
	public float stiffness = 1;
	public float damping = 0;
	
	public void set(float stiffness, float damping){
		this.stiffness = stiffness;
		this.damping = damping;
	}
	
	public void setCriticalDamping(float mass, float stiffness){
		this.stiffness = stiffness;
		damping = (float) Math.sqrt(4*mass*stiffness);
	}
	public void setForCriticalDamping(float ossicilationPeroid, float halflife, float mass){
		stiffness = (float) (4*Math.PI*Math.PI*ossicilationPeroid*ossicilationPeroid*mass);
	}
	
	public float getFrequence(float mass){
		return (float)(Math.sqrt(stiffness/mass))/MathUtils.PI2;
	}
	
	public float getDampingRation(float mass){
		return (float) (damping/(2*Math.sqrt(mass*stiffness)));
	}
	
	public float getTimeConstant(float mass){
		return (float)(2*mass/damping);
	}
	
}