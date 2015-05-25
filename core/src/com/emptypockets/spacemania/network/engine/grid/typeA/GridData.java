package com.emptypockets.spacemania.network.engine.grid.typeA;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.utils.event.EventRecorder;

public class GridData {
	Spring[] springs;
	PointMass[][] points;
	GridSettings settings;
	Rectangle region;

	public void createGrid(Rectangle region, GridSettings settings) {
		this.points = new PointMass[settings.numX][settings.numY];
		this.region = region;
		this.settings = settings;

		ArrayList<Spring> springList = new ArrayList<Spring>();
		PointMass[][] fixedPoints = new PointMass[settings.numX][settings.numY];

		int column = 0;
		int row = 0;
		float inverseMass = 0;
		float nodeDamping = settings.nodeDamping;
		float xLoc = 0;
		float yLoc = 0;

		for (int xP = 0; xP < settings.numX; xP++) {
			xLoc = region.x + region.width * (xP / (settings.numX - 1f));
			for (int yP = 0; yP < settings.numY; yP++) {
				yLoc = region.y + region.height * (yP / (settings.numY - 1f));

				if (row == 0 || column == 0 || row == settings.numX - 1 || column == settings.numY - 1) {
					inverseMass = settings.edge.inverseMass;
				} else if (row % settings.minorCountX == 0 || column % settings.minorCountY == 0) {
					inverseMass = settings.major.inverseMass;
				} else {
					inverseMass = settings.minor.inverseMass;
				}

				points[row][column] = new PointMass(new Vector2(xLoc, yLoc), inverseMass);
				points[row][column].setDefaultDamping(nodeDamping);

				fixedPoints[row][column] = new PointMass(new Vector2(xLoc, yLoc), 0);
				fixedPoints[row][column].setDefaultDamping(nodeDamping);
				column++;
			}
			row++;
			column = 0;
		}

		// link the point masses with springs
		for (int xP = 0; xP < settings.numX; xP++)
			for (int yP = 0; yP < settings.numY; yP++) {

				if (xP == 0 || yP == 0 || xP == settings.numX - 1 || yP == settings.numY - 1) {
					// Edge
					springList.add(new Spring(fixedPoints[xP][yP], points[xP][yP], settings.edge.stiffness, settings.edge.damping));
				} else if (xP % settings.minorCountX == 0 && yP % settings.minorCountY == 0) {
					// Major
					springList.add(new Spring(fixedPoints[xP][yP], points[xP][yP], settings.major.stiffness, settings.major.damping));
				}

				if (xP > 0)
					springList.add(new Spring(points[xP - 1][yP], points[xP][yP], settings.minor.stiffness, settings.minor.damping));
				if (yP > 0)
					springList.add(new Spring(points[xP][yP - 1], points[xP][yP], settings.minor.stiffness, settings.minor.damping));
			}

		springs = springList.toArray(new Spring[springList.size()]);
	}

	public void applyDirectedForce(Vector2 force, Vector2 position, float radius) {
		Vector2 forceTmp = new Vector2();
		float dist2Tmp = 0;

		for (int x = 0; x < points.length; x++) {
			for (int y = 0; y < points[x].length; y++) {
				PointMass mass = points[x][y];
				dist2Tmp = mass.pos.dst2(position);
				if (dist2Tmp < radius * radius) {
					forceTmp.set(force);
					forceTmp.scl(10 / (10 + (float) Math.sqrt(dist2Tmp)));
					mass.applyForce(forceTmp);
				}
			}
		}
	}

	public void applyImplosiveForce(float force, Vector2 position, float radius) {
		Vector2 forceTmp = new Vector2();
		float dist2Tmp = 0;

		for (int x = 0; x < points.length; x++) {
			for (int y = 0; y < points[x].length; y++) {
				PointMass mass = points[x][y];

				dist2Tmp = mass.pos.dst2(position);
				if (dist2Tmp < radius * radius) {
					forceTmp.set(position);
					forceTmp.sub(mass.pos);
					float constVal = 10;
					float val = constVal * force / (constVal * constVal * constVal + dist2Tmp);
					forceTmp.scl(val);
					mass.applyForce(forceTmp);
					mass.increaseDamping(0.6f);
				}
			}
		}
	}

	public void applyExplosiveForce(float force, Vector2 pos, float radius) {
		Vector2 forceTmp = new Vector2();
		float dist2Tmp = 0;
		float constVal = 10;
		for (int x = 0; x < points.length; x++) {
			for (int y = 0; y < points[x].length; y++) {
				PointMass mass = points[x][y];
				constVal = mass.inverseMass;
				dist2Tmp = mass.pos.dst2(pos);
				if (dist2Tmp < radius * radius) {
					forceTmp.set(mass.pos);
					forceTmp.sub(pos);
					float val = constVal * force / (constVal * constVal * constVal + dist2Tmp);
					forceTmp.scl(val);
					mass.applyForce(forceTmp);
					mass.increaseDamping(0.6f);
				}
			}
		}
	}

	public void render(ShapeRenderer shape) {
		boolean circle = false;
		boolean line = true;

		if (circle) {
			shape.begin();
			for (int xP = 0; xP < points.length; xP++) {
				for (int yP = 0; yP < points[xP].length; yP++) {

					if (xP == 0 || yP == 0 || xP == settings.numX - 1 || yP == settings.numY - 1) {
						// Edge
						shape.setColor(1f, 0f, 0f, 1f);
					} else if (xP % settings.minorCountX == 0 && yP % settings.minorCountY == 0) {
						// Major
						shape.setColor(0f, 1f, 0f, 1f);
					} else {
						shape.setColor(0f, 0f, 1f, 1f);
					}
					shape.circle(points[xP][yP].pos.x, points[xP][yP].pos.y, 1, 10);
				}
			}
			shape.end();
		}
		if (line) {
			shape.setColor(.1f, .1f, 1f, 0.4f);
			shape.begin(ShapeType.Line);
			for (int x = 0; x < points.length; x++) {
				for (int y = 0; y < points[x].length; y++) {

					Vector2 left;
					Vector2 up;

					Vector2 p = (points[x][y].pos);
					if (x >= 1) {
						left = (points[x - 1][y].pos);
						shape.line(left.x, left.y, p.x, p.y);
					}
					if (y >= 1) {
						up = points[x][y - 1].pos;
						shape.line(up.x, up.y, p.x, p.y);
					}
				}
			}
			shape.end();
		}
	}

	public void update(float time) {
		for (Spring spring : springs) {
			spring.update();
		}

		for (PointMass[] data : points) {
			for (PointMass point : data) {
				point.update(time);
			}
		}
	}

	public void dispose() {
		points = null;
		springs = null;
	}

	public static void main(String input[]) {

		GridSettings set = new GridSettings();

		float expRange = 1e10f;
		float expForce = 1e4f;

		int sizeX = 1000;
		int sizeY = 1000;
		int numX = 400;
		int numY = 400;

		Rectangle bounds = new Rectangle();
		bounds.x = -sizeX / 2;
		bounds.y = -sizeY / 2;
		bounds.width = sizeX;
		bounds.height = sizeY;

		set.numX = numX;
		set.numY = numY;

		set.minorCountX = 3;
		set.minorCountY = 3;

		set.minor.inverseMass = 1f;
		set.minor.damping = 1e-2f;
		set.minor.stiffness = 2e1f;

		set.major.inverseMass = 1f;
		set.major.damping = 1e-2f;
		set.major.stiffness = 2e1f;

		set.edge.inverseMass = 0f;
		set.edge.damping = 0.01f;
		set.edge.stiffness = 1e4f;

		int number = 500;
		EventRecorder event = new EventRecorder(number);

		event.begin("CREATE");
		GridData data = new GridData();
		data.createGrid(bounds, set);
		event.end("CREATE");

		Vector2 force = new Vector2(expForce, expForce);
		Vector2 pos = new Vector2(0, 0);

		for (int i = 0; i < number * 2; i++) {
			event.begin("FORCE");
			data.applyDirectedForce(force, pos, 10 * sizeX);
			event.end("FORCE");

			event.begin("SOLVE");
			data.update(1);
			event.end("SOLVE");
		}
		System.out.printf("%e,%e\n", event.getAverageEventDuration("FORCE"), event.getAverageEventDuration("SOLVE"));
	}
}