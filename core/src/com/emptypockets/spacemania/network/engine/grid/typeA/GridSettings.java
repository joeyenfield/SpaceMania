package com.emptypockets.spacemania.network.engine.grid.typeA;
public class GridSettings {
	public int numX = 100;
	public int numY = 100;
	public int minorCountX = 10;
	public int minorCountY = 10;
	public float nodeDamping = 0.64f;
	public SpringSettings edge = new SpringSettings();
	public SpringSettings major = new SpringSettings();
	public SpringSettings minor = new SpringSettings();
}

class SpringSettings{
	public float stiffness = 0;
	public float damping = 0;
	public float inverseMass = 0;
}