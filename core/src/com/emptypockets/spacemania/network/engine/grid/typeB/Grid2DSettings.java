package com.emptypockets.spacemania.network.engine.grid.typeB;

import com.badlogic.gdx.math.Rectangle;

public class Grid2DSettings {
	public Rectangle bounds = new Rectangle();
	public int numX;
	public int numY;
	public NodeLinkSettings edge = new NodeLinkSettings();
	
	public NodeLinkSettings ankorAlt = new NodeLinkSettings();
	public NodeLinkSettings ankor = new NodeLinkSettings();
	
	public NodeLinkSettings links = new NodeLinkSettings();
	
	public float inverseMass;
}