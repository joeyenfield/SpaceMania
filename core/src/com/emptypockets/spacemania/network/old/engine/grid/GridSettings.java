package com.emptypockets.spacemania.network.old.engine.grid;

import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.network.old.engine.grid.spring.NodeLinkSettings;

public class GridSettings {
	public Rectangle bounds = new Rectangle();
	public int numX;
	public int numY;
	public NodeLinkSettings edge = new NodeLinkSettings();
	public NodeLinkSettings ankor = new NodeLinkSettings();
	public NodeLinkSettings links = new NodeLinkSettings();
	
	public float inverseMass;
}