package com.emptypockets.spacemania.network.engine.grid.typeB;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GridData2D {
	GridNode[][] nodes;
	ArrayList<NodeLink> links = new ArrayList<NodeLink>();
	public Grid2DSettings set;

	boolean renderGrid = true;
	boolean subSample = false;
	int sampleCount = 1;

	Vector2 tempSubSampleRec = new Vector2();
	Object lock = new Object();

	Color majorColor = new Color(.1f, .1f, 1f, .3f);
	Color gridColor = new Color(.3f, .3f, 1f, .3f);

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

	public Grid2DSettings getSettings() {
		return set;
	}

	public void createGrid(Grid2DSettings set) {
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
					NodeLinkSettings cfg;
					if (x == 0 || y == 0 || x == set.numX - 1 || y == set.numY - 1) {
						cfg = set.edge;
					} else if (x % 3 == 0 && y % 3 == 0) {
						cfg = set.ankorAlt;
					} else {
						cfg = set.ankor;
					}

					links.add(new FixedNodeLink(nodes[x][y], cfg));

					cfg = set.links;
					if (x > 0) {
						links.add(new DualNodeLink(nodes[x][y], nodes[x - 1][y], cfg));
					}
					if (y > 0) {
						links.add(new DualNodeLink(nodes[x][y], nodes[x][y - 1], cfg));
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
					if (lenght < radius * radius) {
						dir.scl(100 * force / (10000f + lenght));
						nodes[x][y].applyImpulse(dir);
						nodes[x][y].increaseDamping(0.6f);
					}
				}
			}
		}
	}

	public void update(float delta) {
		synchronized (lock) {
			for (GridNode[] nodeData : nodes) {
				for (GridNode node : nodeData) {
					node.update();
				}
			}
		}
	}

	public void render(ShapeRenderer shape, Vector2 screenStart, Vector2 screenEnd, float zoom) {
		if (!renderGrid) {
			return;
		}
		synchronized (lock) {
			int xMin = (int) (set.numX * ((screenStart.x - set.bounds.x) / set.bounds.width));
			int xMax = (int) (set.numX * ((screenEnd.x - set.bounds.x) / set.bounds.width)) + 1;
			int yMin = (int) (set.numY * ((screenStart.y - set.bounds.y) / set.bounds.height));
			int yMax = (int) (set.numY * ((screenEnd.y - set.bounds.y) / set.bounds.height)) + 1;
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

			Vector2 pA = null;
			Vector2 pB = null;
			Vector2 pC = null;
			Vector2 pD = null;

			int minX = 5;
			int minY = 5;

			/**
			 * Draw Major Grid lines
			 */
			shape.setColor(majorColor);
			shape.begin(ShapeType.Line);

			CatmullRomSpline spline = new CatmullRomSpline();

			Vector2 linearM = new Vector2();
			Vector2 posAE;
			Vector2 posA;
			Vector2 posM = new Vector2();
			Vector2 posB;
			Vector2 posBE;
			for (int x = xMin; x < xMax; x++) {
				for (int y = yMin; y < yMax; y++) {

					// Draw Horizontal Line
					if (x - 1 >= 0) {
						posA = nodes[x - 1][y].pos;
						posB = nodes[x][y].pos;
						linearM.x = (posB.x + posA.x) / 2;
						linearM.y = (posB.y + posA.y) / 2;

						// Check Interpolation
						if (x - 2 >= 0 && x + 1 < set.numX) {
							// Get Extended Points
							posAE = nodes[x - 2][y].pos;
							posBE = nodes[x + 1][y].pos;
							spline.set(new Vector2[] { posAE, posA, posB, posBE }, false);
							spline.valueAt(posM, 0.5f);
							// Draw Interpolation - if needed
							// System.out.println(linearM.toString()+" : "+posM+" : "+linearM.dst2(posM));
							if (linearM.dst2(posM) > 1) {
								// Two lines
								shape.line(posA.x, posA.y, posM.x, posM.y);
								shape.line(posM.x, posM.y, posB.x, posB.y);
							} else {
								// One line
								shape.line(posA.x, posA.y, posB.x, posB.y);
							}
						} else {
							// Sinigle Line
							shape.line(posA.x, posA.y, posB.x, posB.y);
						}
					}

					// Draw Vertical Line
					if (y - 1 >= 0) {
						posA = nodes[x][y - 1].pos;
						posB = nodes[x][y].pos;
						linearM.x = (posB.x + posA.x) / 2;
						linearM.y = (posB.y + posA.y) / 2;

						// Check Interpolation
						if (y - 2 >= 0 && y + 1 < set.numY) {
							// Get Extended Points
							posAE = nodes[x][y - 2].pos;
							posBE = nodes[x][y + 1].pos;
							spline.set(new Vector2[] { posAE, posA, posB, posBE }, false);
							spline.valueAt(posM, 0.5f);
							// Draw Interpolation - if needed
							// System.out.println(linearM.toString()+" : "+posM+" : "+linearM.dst2(posM));
							if (linearM.dst2(posM) > 1) {
								// Two lines
								shape.line(posA.x, posA.y, posM.x, posM.y);
								shape.line(posM.x, posM.y, posB.x, posB.y);
							} else {
								// One line
								shape.line(posA.x, posA.y, posB.x, posB.y);
							}
						} else {
							// Sinigle Line
							shape.line(posA.x, posA.y, posB.x, posB.y);
						}
					}

					// if ( x == 0 || y == 0 || x == nodes.length - 1 || y ==
					// // nodes[x].length - 1) {
					// pD = (nodes[x][y].pos);
					// shape.setColor(majorColor);
					// if (x > 0 && (y == 0 || y % minY == 0)) {
					// pC = nodes[x - 1][y].pos;
					// shape.line(pD.x, pD.y, pC.x, pC.y);
					// }
					// if (y > 0 && (x == 0 || x % minX == 0)) {
					// pB = nodes[x][y - 1].pos;
					// shape.line(pD.x, pD.y, pB.x, pB.y);
					// }
				}
			}
			shape.end();

			/**
			 * Draw Sub Samples lines
			 */
			if (subSample) {
				shape.begin(ShapeType.Line);
				shape.setColor(gridColor);
				for (int x = xMin; x < xMax; x++) {
					for (int y = yMin; y < yMax; y++) {
						pD = (nodes[x][y].pos);
						// Fills in the grid
						if (x > 0 && y > 0) {
							pA = nodes[x - 1][y - 1].pos;
							pC = nodes[x - 1][y].pos;
							pB = nodes[x][y - 1].pos;
							drawRec(shape, pA, pB, pC, pD, 0, this.sampleCount);
						}
					}
				}
			}
			shape.end();

			/**
			 * Draw Grid lines
			 */
			shape.begin(ShapeType.Line);
			for (int x = xMin; x < xMax; x++) {
				for (int y = yMin; y < yMax; y++) {

					shape.setColor(gridColor);
					pD = (nodes[x][y].pos);
					if (x > 0) {
						pC = nodes[x - 1][y].pos;
						shape.line(pD.x, pD.y, pC.x, pC.y);
					}
					if (y > 0) {
						pB = nodes[x][y - 1].pos;
						shape.line(pD.x, pD.y, pB.x, pB.y);
					}
				}
			}
			shape.end();
		}
	}

	public void drawRec(ShapeRenderer shape, Vector2 pA, Vector2 pB, Vector2 pC, Vector2 pD, int depth, int maxDepth) {
		if (depth == maxDepth) {
			tempSubSampleRec.x = (pA.x + pB.x + pC.x + pD.x) / 4;
			tempSubSampleRec.y = (pA.y + pB.y + pC.y + pD.y) / 4;
			// Left
			shape.line(tempSubSampleRec.x, tempSubSampleRec.y, (pA.x + pC.x) / 2, (pA.y + pC.y) / 2);
			shape.line(tempSubSampleRec.x, tempSubSampleRec.y, (pA.x + pB.x) / 2, (pA.y + pB.y) / 2);
			shape.line(tempSubSampleRec.x, tempSubSampleRec.y, (pB.x + pD.x) / 2, (pB.y + pD.y) / 2);
			shape.line(tempSubSampleRec.x, tempSubSampleRec.y, (pD.x + pC.x) / 2, (pD.y + pC.y) / 2);
		} else {
			drawRec(shape, pA.x, pA.y, pB.x, pB.y, pC.x, pC.y, pD.x, pD.y, depth, maxDepth);
		}
	}

	public void drawRec(ShapeRenderer shape, float xA, float yA, float xB, float yB, float xC, float yC, float xD, float yD, int depth, int maxDepth) {
		if (depth == maxDepth) {
			shape.line(xA, yA, xB, yB);
			// shape.line(xB, yB, xD, yD);
			// shape.line(xC, yC, xD, yD);
			shape.line(xC, yC, xA, yA);
		} else {
			float xM = (xA + xB + xC + xD) / 4;
			float yM = (yA + yB + yC + yD) / 4;
			drawRec(shape, xA, yA, (xA + xB) / 2, (yA + yB) / 2, (xA + xC) / 2, (yA + yC) / 2, xM, yM, depth + 1, maxDepth);
			drawRec(shape, (xA + xB) / 2, (yA + yB) / 2, xB, yB, xM, yM, (xB + xD) / 2, (yB + yD) / 2, depth + 1, maxDepth);
			drawRec(shape, (xA + xC) / 2, (yA + yC) / 2, xM, yM, xC, yC, (xC + xD) / 2, (yC + yD) / 2, depth + 1, maxDepth);
			drawRec(shape, xM, yM, (xB + xD) / 2, (yB + yD) / 2, (xC + xD) / 2, (yC + yD) / 2, xD, yD, depth + 1, maxDepth);
		}
	}

	public static void main(String input[]) {

		CatmullRomSpline spline = new CatmullRomSpline();

		Vector2 linearM = new Vector2();
		Vector2 posM = new Vector2();

		Vector2 posAE = new Vector2(0, 0);
		;
		Vector2 posA = new Vector2(1, 0);
		;
		Vector2 posB = new Vector2(2, 0);
		;
		Vector2 posBE = new Vector2(3, 0);
		;

		linearM.x = (posB.x + posA.x) / 2;
		linearM.y = (posB.y + posA.y) / 2;

		spline.set(new Vector2[] { posAE, posA, posB, posBE }, false);
		spline.valueAt(posM, 0.5f);

		System.out.println(linearM);
		System.out.println(posM);

	}
}