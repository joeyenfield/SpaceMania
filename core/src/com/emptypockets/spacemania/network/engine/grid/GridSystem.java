package com.emptypockets.spacemania.network.engine.grid;

import java.util.ArrayList;

import sun.security.util.Length;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.engine.grid.spring.DualNodeLink;
import com.emptypockets.spacemania.network.engine.grid.spring.FixedNodeLink;
import com.emptypockets.spacemania.network.engine.grid.spring.NodeLink;
import com.emptypockets.spacemania.network.engine.grid.spring.NodeLinkSettings;

public class GridSystem {
	public GridNode[][] nodes;
	public ArrayList<NodeLink> links = new ArrayList<NodeLink>();
	public GridSettings set;

	Vector2 tempSubSampleRec = new Vector2();
	Object lock = new Object();

	public void move(Rectangle region) {
		float xP, yP;
		float xV;
		set.bounds.set(region);
		for (int x = 0; x < set.numX; x++) {
			for (int y = 0; y < set.numY; y++) {
				xV = x;
				xP = set.bounds.x + set.bounds.width * (xV / (set.numX - 1f));
				yP = set.bounds.y + set.bounds.height * (y / (set.numY - 1f));
				nodes[x][y].pos.set(xP, yP);
				nodes[x][y].restPos.set(xP, yP);
			}
		}
		for (NodeLink node : links) {
			node.updateRestPos();
		}
	}

	public GridSettings getSettings() {
		return set;
	}

	public Vector2 getNodePos(int x, int y) {
		return nodes[x][y].pos;
	}

	public void createGrid(GridSettings set) {
		this.set = set;
		dispose();
		synchronized (lock) {
			this.set = set;

			// Create Nodes
			nodes = new GridNode[set.numX][set.numY];
			links = new ArrayList<NodeLink>(set.numX * set.numY * 2);
			for (int x = 0; x < set.numX; x++) {
				for (int y = 0; y < set.numY; y++) {
					nodes[x][y] = new GridNode();
					if (x == 0 || y == 0 || x == set.numX - 1 || y == set.numY - 1) {
						nodes[x][y].inverseMass = 0;
					} else {
						nodes[x][y].inverseMass = set.inverseMass;
					}
				}
			}

			for (int x = 0; x < set.numX; x++) {
				for (int y = 0; y < set.numY; y++) {
					if (x == 0 || y == 0 || x == set.numX - 1 || y == set.numY - 1) {
						links.add(new FixedNodeLink(nodes[x][y], set.edge));
					} else {// if (x % 3 == 0 || y % 3 == 0) {
						links.add(new FixedNodeLink(nodes[x][y], set.ankor));
					}
					if (x > 0) {
						links.add(new DualNodeLink(nodes[x][y], nodes[x - 1][y], set.links));
					}
					if (y > 0) {
						links.add(new DualNodeLink(nodes[x][y], nodes[x][y - 1], set.links));
					}
				}
			}
			move(set.bounds);
		}
	}

	public void dispose() {
		synchronized (lock) {
			nodes = null;
			set = null;
			links.clear();
			links.trimToSize();
		}
	}

	public void resetNodes() {
		synchronized (lock) {
			for (NodeLink link : links) {
				if (link instanceof FixedNodeLink) {
					FixedNodeLink lk = (FixedNodeLink) link;
					lk.reset();
				}
			}
		}
	}

	public void solve() {
		synchronized (lock) {
			for (NodeLink link : links) {
				link.solve();
			}
		}
	}

	// public void applyDirectedForce(Vector2 force, Vector2 position, float
	// radius) {
	// for (int x=0; x<points.length; x++) {
	// for (int y=0; y<points[0].length; y++) {
	// if (position.distanceSquared(points[x][y].getPosition()) < radius *
	// radius) {
	// float forceFactor = 10 / (10 +
	// position.distance(points[x][y].getPosition()));
	// points[x][y].applyForce(force.mult(forceFactor));
	// }
	// }
	// }
	// }
	//
	// public void applyImplosiveForce(float force, Vector3f position, float
	// radius) {
	// for (int x=0; x<points.length; x++) {
	// for (int y=0; y<points[0].length; y++) {
	// float dist = position.distanceSquared(points[x][y].getPosition());
	// if (dist < radius * radius) {
	// Vector3f forceVec = position.subtract(points[x][y].getPosition());
	// forceVec.multLocal(10f * force / (100 + dist));
	// points[x][y].applyForce(forceVec);
	// points[x][y].increaseDamping(0.6f);
	// }
	// }
	// }
	// }
	//
	// public void applyExplosiveForce(float force, Vector3f position, float
	// radius) {
	// for (int x=0; x<points.length; x++) {
	// for (int y=0; y<points[0].length; y++) {
	// float dist = position.distanceSquared(points[x][y].getPosition());
	// if (dist < radius * radius) {
	// Vector3f forceVec = position.subtract(points[x][y].getPosition());
	// forceVec.multLocal(-100f * force / (10000 + dist));
	// points[x][y].applyForce(forceVec);
	// points[x][y].increaseDamping(0.6f);
	// }
	// }
	// }
	// }

	public void applyExplosion(Vector2 pos, float force, float radius) {
		synchronized (lock) {
			int xMin = (int) (set.numX * ((pos.x - radius - set.bounds.x) / set.bounds.width));
			int xMax = (int) (set.numX * ((pos.x + radius - set.bounds.x) / set.bounds.width)) + 1;
			int yMin = (int) (set.numY * ((pos.y - radius - set.bounds.y) / set.bounds.height));
			int yMax = (int) (set.numY * ((pos.y + radius - set.bounds.y) / set.bounds.height)) + 1;

			if (xMin < 0) {
				xMin = 0;
			}
			if (yMin < 0) {
				yMin = 0;
			}
			if (xMax > set.numX) {
				xMax = set.numX;
			}
			if (yMax > set.numY) {
				yMax = set.numY;
			}
			Vector2 dir = new Vector2();
			float lenght = 0;
			for (int x = xMin; x < xMax; x++) {
				for (int y = yMin; y < yMax; y++) {
					dir.x = nodes[x][y].pos.x - pos.x;
					dir.y = nodes[x][y].pos.y - pos.y;
					lenght = dir.len2();
//					if (lenght < radius * radius) {
						dir.scl(100 * force / (10000f + lenght));
						nodes[x][y].applyImpulse(dir);
						nodes[x][y].increaseDamping(0.6f);
//					}
				}
			}
		}
	}

	public void update() {
		synchronized (lock) {
			if (nodes == null) {
				return;
			}
			for (GridNode[] nodeData : nodes) {
				for (GridNode node : nodeData) {
					node.update();
				}
			}
		}
	}

	public int getSizeX() {
		return set.numX;
	}

	public int getSizeY() {
		return set.numY;
	}

	public GridNode getNode(int x, int y) {
		return nodes[x][y];
	}
}