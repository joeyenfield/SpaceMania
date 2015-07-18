package com.emptypockets.spacemania.gui.renderer.engine;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Path {
	ArrayList<Vector2> points = new ArrayList<Vector2>();
	Vector2 temp1 = new Vector2();
	Vector2 temp2 = new Vector2();
	Rectangle bounds = new Rectangle();
	int renderSegment = 0;
	float thickness = 1;
	Color color = new Color();

	public void updateBounds() {
		float minX = points.get(0).x;
		float maxX = points.get(0).x;
		float minY = points.get(0).y;
		float maxY = points.get(0).y;
		for (Vector2 v : points) {
			minX = Math.min(minX, v.x);
			maxX = Math.max(maxX, v.x);
			minY = Math.min(minY, v.y);
			maxY = Math.max(maxY, v.y);
		}
		bounds.x = minX;
		bounds.width = maxX - minX;
		bounds.y = minY;
		bounds.height = maxY - minY;
	}

	public void setColor(Color c) {
		this.color.set(c);
	}

	public Color getColor() {
		return color;
	}

	public void clear() {
		points.clear();
	}

	public void start(Vector2 pos) {
		clear();
		addPoint(pos);
	}

	public void addPoint(Vector2 pos) {
		points.add(pos);
	}

	public void smartLineTo(Vector2 pNew, float delta) {
		boolean newLine = true;
		if (points.size() >= 2) {
			Vector2 pLast = points.get(points.size() - 1);
			Vector2 pOlder = points.get(points.size() - 2);

			temp1.set(pOlder).sub(pLast);
			temp2.set(pNew).sub(pLast);

			float normalLength = (float) Math.sqrt(temp1.x * temp1.x + temp1.y * temp1.y);
			float dist = Math.abs(temp2.x * temp1.y - temp2.y * temp1.x) / normalLength;
			if (dist < delta) {
				float dotProd = (float) (Math.acos(temp1.dot(temp2) / (temp1.len() * temp2.len())));
				if (dotProd < 0) {
					pLast.set(pNew);
					newLine = false;
				}
			}
		}

		if (newLine) {
			addPoint(pNew);
		}
	}

	public static void main(String args[]) {
		Path path;

		System.out.println("Along X");
		path = new Path();
		path.start(new Vector2(0, 0));
		path.smartLineTo(new Vector2(1, 0), 1);
		path.smartLineTo(new Vector2(2, 0), 1);
		path.smartLineTo(new Vector2(3, 0), 1);
		path.smartLineTo(new Vector2(4, 0), 1);

		System.out.println("");
		System.out.println("Along Y");
		path = new Path();
		path.start(new Vector2(0, 0));
		path.smartLineTo(new Vector2(0, 1), 1);
		path.smartLineTo(new Vector2(0, 2), 1);
		path.smartLineTo(new Vector2(0, 3), 1);
		path.smartLineTo(new Vector2(0, 4), 1);
		path.smartLineTo(new Vector2(0, 3), 1);
		path.smartLineTo(new Vector2(0, 10), 1);

	}

	public Rectangle getBounds() {
		return bounds;
	}

	public int getSegment(int renderSegment, Vector2 p1, Vector2 p2, float delta) {
		if (renderSegment + 1 >= points.size()) {
			renderSegment = -1;
		} else {
			p1.set(points.get(renderSegment));
			p2.set(points.get(renderSegment + 1));

			renderSegment = renderSegment + 1;
			boolean found = true;
			while (found && renderSegment + 1 < points.size()) {
				found = false;
				Vector2 pLast = points.get(renderSegment);
				Vector2 pNew = points.get(renderSegment + 1);

				temp1.set(p1).sub(pLast);
				temp2.set(pNew).sub(pLast);

				float normalLength = (float) Math.sqrt(temp1.x * temp1.x + temp1.y * temp1.y);
				float dist = Math.abs(temp2.x * temp1.y - temp2.y * temp1.x) / normalLength;
				if (dist < delta) {
					float dotProd = (float) (Math.acos(temp1.dot(temp2) / (temp1.len() * temp2.len())));
					if (dotProd < 0) {
						p2.set(pNew);
						renderSegment++;
						found = true;
					}
				}
			}
		}

		return renderSegment;
	}

	public int size() {
		return points.size();
	}

	public float getThickness() {
		return thickness;
	}

	public void setThickness(float thickness) {
		this.thickness = thickness;
	}
}
