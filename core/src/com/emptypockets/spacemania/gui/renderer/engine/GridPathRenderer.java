package com.emptypockets.spacemania.gui.renderer.engine;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.engine.grid.GridNode;
import com.emptypockets.spacemania.network.engine.grid.GridSystem;
import com.emptypockets.spacemania.network.engine.grid.GridSystemListener;
import com.emptypockets.spacemania.network.engine.grid.spring.NodeLink;

public class GridPathRenderer implements GridSystemListener{
	ArrayList<Path> paths = new ArrayList<Path>();

	public void init(){
		
	}
	public void rebuild(GridSystem grid) {
		paths.clear();
		float alpha = 0.2f;
		// Build Y Paths
		for (int x = 0; x < grid.getSizeX(); x++) {
			Path p = new Path();
			if(x%4 == 0){
				p.setColor(Color.CYAN);
				p.setThickness(3f);
			}else{
				p.setColor(Color.TEAL);
				p.setThickness(2f);
			}
			p.getColor().a = alpha;
			for (int y = 0; y < grid.getSizeY(); y++) {
				p.addPoint(grid.getNodePos(x, y));
			}
			paths.add(p);
		}

		// Build X Paths
		for (int y = 0; y < grid.getSizeY(); y++) {
			Path p = new Path();
			if(y%4 == 0){
				p.setColor(Color.CYAN);
				p.setThickness(3f);
			}else{
				p.setColor(Color.TEAL);
				p.setThickness(2f);
			}
			p.getColor().a = alpha;
			for (int x = 0; x < grid.getSizeX(); x++) {
				p.addPoint(grid.getNodePos(x, y));
			}
			paths.add(p);
		}
	}

	public void updateBounds() {
		for (Path p : paths) {
			p.updateBounds();
		}
	}

	public void renderPath(ShapeRenderer render, Path p) {
		Vector2 p1 = new Vector2();
		Vector2 p2 = new Vector2();

		render.setColor(p.getColor());
		int renderSegment = 0;
		boolean hasNext = true;
		while (hasNext) {
			renderSegment = p.getSegment(renderSegment, p1, p2, 1);
			hasNext = renderSegment >= 0;
			if (hasNext) {
				render.rectLine(p1, p2, p.getThickness());
			}
		}
	}

	public void render(GridSystem data, Rectangle viewport, ShapeRenderer render) {

		render.begin(ShapeType.Filled);

		for (Path p : paths) {
			if (viewport.overlaps(p.getBounds())) {
				renderPath(render, p);
			}
		}
		render.end();

	}

	public void renderNode(ShapeRenderer render, GridNode nodeA) {
		render.setColor(Color.OLIVE);
		render.circle(nodeA.restPos.x, nodeA.restPos.y, 8);
		render.setColor(Color.ORANGE);
		render.circle(nodeA.pos.x, nodeA.pos.y, 5);
	}

	public void renderLink(ShapeRenderer render, NodeLink link) {
		float force = link.getForce() / 100;
		if (force < 0) {
			render.setColor(Color.RED);
			force *= -1;
		} else {
			render.setColor(Color.GREEN);
		}
		render.rectLine(link.getP1(), link.getP2(), 1 + force);
		
		render.setColor(Color.PURPLE);
	}

	@Override
	public void gridChanged(GridSystem grid) {
		this.rebuild(grid);
	}

}
