package com.emptypockets.spacemania.metrics.events.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.emptypockets.spacemania.gui.tools.TextRender;
import com.emptypockets.spacemania.metrics.events.Event;
import com.emptypockets.spacemania.metrics.events.EventSystem;
import com.emptypockets.spacemania.utils.CameraHelper;
import com.emptypockets.spacemania.utils.GraphicsToolkit;
import com.emptypockets.spacemania.utils.RectangeUtils;

public class EventRender {
	CameraHelper helper = new CameraHelper();
	public long startTime = 0;
	public long endTime = 0;
	public float eventHeight = 1;
	public float eventGap = 0;

	long maxTickDelta = 1;
	long maxGridCount = 10;

	Rectangle dataViewport = new Rectangle(0, 0, 1000, 1000);
	Rectangle eventBounds = new Rectangle();

	Rectangle displayAxisBounds = new Rectangle();
	TextRender text = new TextRender();

	Vector2 axisPoint1 = new Vector2();
	Vector2 axisPoint2 = new Vector2();

	Color[] colorCycle = new Color[] { Color.WHITE, Color.RED, Color.BLUE, Color.PURPLE, Color.PINK, Color.ORANGE };

	public void drawScene(Camera camera, ShapeRenderer render, Rectangle screen) {
		float textHeight = helper.getScreenToCameraPixelX(camera, 30);
		eventHeight = helper.getScreenToCameraPixelX(camera, 35);

		Event event = EventSystem.getEventSystem().getRoot();

		render.begin(ShapeType.Filled);
		renderEventBox(render, event, 0, 0, screen);
		render.end();

		render.begin(ShapeType.Line);
		renderEventBox(render, event, 0, 1, screen);
		render.end();

		render.begin(ShapeType.Filled);
		render.setColor(Color.LIGHT_GRAY);
		renderTextBox(camera, render, event, 0, screen, textHeight);
		render.end();

		// Fill Screen intersection with viewport with axis of 10 segments
		RectangeUtils.clampToScreen(screen, dataViewport, displayAxisBounds);
		drawAxisData(render, helper.getScreenToCameraPixelX(camera, 1), helper.getScreenToCameraPixelX(camera, 5), helper.getScreenToCameraPixelX(camera, 10), screen);
	}

	public void drawAxisData(ShapeRenderer render, float lineThick, float borderThick, float textSize, Rectangle viewport) {
		int linesX = 10;
		int linesY = 10;

		render.begin(ShapeType.Filled);
		GraphicsToolkit.drawRectangeLine(render, displayAxisBounds, borderThick);

		long lastValue = 0;
		for (int x = 0; x < linesX; x++) {
			float progress = x / (linesX - 1f);
			axisPoint1.y = displayAxisBounds.y;
			axisPoint2.y = displayAxisBounds.y + displayAxisBounds.height;
			axisPoint1.x = axisPoint2.x = displayAxisBounds.x + displayAxisBounds.width * progress;

			long value = (long) ((endTime - startTime) * RectangeUtils.xProgress(dataViewport, axisPoint1.x));
			if (value != lastValue || x == 0) {
				render.rectLine(axisPoint1, axisPoint2, lineThick);
				text.render(render, "" + value, axisPoint1.x, axisPoint1.y + 2 * borderThick, textSize, viewport);
				lastValue = value;
			}
		}

		for (int y = 0; y < linesY; y++) {
			float progress = y / (linesY - 1f);
			axisPoint1.x = displayAxisBounds.x;
			axisPoint2.x = displayAxisBounds.x + displayAxisBounds.width;
			axisPoint1.y = axisPoint2.y = displayAxisBounds.y + displayAxisBounds.height * progress;
			render.rectLine(axisPoint1, axisPoint2, lineThick);
		}
		render.end();
	}

	public void renderEventBox(ShapeRenderer render, Event event, int depth, int colorNum, Rectangle screen) {
		updateEventBounds(event, depth);

		// If No X - just stop cant overlap
		if (!RectangeUtils.overlapsX(screen, eventBounds)) {
			return;
		}

		// Dont render if no Y
		if (RectangeUtils.overlapsY(screen, eventBounds)) {
			render.setColor(colorCycle[colorNum % colorCycle.length]);
			render.rect(eventBounds.x, eventBounds.y, eventBounds.width, eventBounds.height);
		}
		synchronized (event.getChildren()) {
			int colorCount = colorNum;
			for (Event child : event.getChildren()) {
				renderEventBox(render, child, depth + 1, colorCount++, screen);
			}
		}
	}

	private void updateEventBounds(Event event, int depth) {
		float start = ((float) (event.getStartTime() - startTime) / (float) (endTime - startTime));
		float end = 1;
		if (event.getEndTime() != 0) {
			end = ((float) (event.getEndTime() - startTime) / (float) (endTime - startTime));
		}
		eventBounds.x = start * dataViewport.width + dataViewport.x;
		eventBounds.width = (end - start) * dataViewport.width;

		eventBounds.y = (depth * eventHeight + eventGap);
		eventBounds.height = eventHeight;
		RectangeUtils.ensurePositive(eventBounds);
	}

	public void renderTextBox(Camera camera, ShapeRenderer render, Event event, int depth, Rectangle screen, float textSize) {
		updateEventBounds(event, depth);

		// If No X - just stop cant overlap
		if (!RectangeUtils.overlapsX(screen, eventBounds)) {
			return;
		}
		if (RectangeUtils.overlapsY(screen, eventBounds)) {
			if (eventBounds.x < screen.x) {
				eventBounds.x = screen.x;
			}

			// Dont render if no Y
			text.render(render, event.getName(), eventBounds.x, eventBounds.y, textSize, eventBounds);
		}

		synchronized (event.getChildren()) {
			for (Event child : event.getChildren()) {
				renderTextBox(camera, render, child, depth + 1, screen, textSize);
			}
		}
	}
}
