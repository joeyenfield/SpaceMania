package com.emptypockets.spacemania.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraHelper {

	Vector3 startPoint = new Vector3();
	Vector3 endPoint = new Vector3();

	public void getBounds(Camera camera, Rectangle bounds) {
		startPoint.x = 0;
		startPoint.y = 0;
		endPoint.x = Gdx.graphics.getWidth();
		endPoint.y = Gdx.graphics.getHeight();

		camera.unproject(startPoint);
		camera.unproject(endPoint);

		bounds.x = startPoint.x;
		bounds.y = startPoint.y;
		bounds.width = endPoint.x - startPoint.x;
		bounds.height = endPoint.y - startPoint.y;

		RectangeUtils.ensurePositive(bounds);
	}

	public float getScreenToCameraPixelX(Camera camera, float size) {
		startPoint.x = 0;
		startPoint.y = 0;
		endPoint.x = size;
		endPoint.y = 0;
		camera.unproject(startPoint);
		camera.unproject(endPoint);
		return Math.abs(startPoint.x - endPoint.x);
	}

	public void screenToWorld(Camera camera, Vector2 tempPos) {
		startPoint.x = tempPos.x;
		startPoint.y = tempPos.y;
		camera.unproject(startPoint);
		tempPos.x = startPoint.x;
		tempPos.y = startPoint.y;
	}
}
