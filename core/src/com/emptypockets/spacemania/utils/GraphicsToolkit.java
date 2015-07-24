package com.emptypockets.spacemania.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class GraphicsToolkit {
    static Color yAxis = new Color(Color.BLUE.r, Color.BLUE.g, Color.BLUE.b, 0.5f);
    static Color xAxis = new Color(Color.RED.r, Color.RED.g, Color.RED.b, 0.5f);

    public static void draw2DAxis(ShapeRenderer shape, OrthographicCamera camera, float gap, Color color) {
        Color grid = new Color(color);


        grid.a = 0.5f;
        GraphicsToolkit.drawGrid(shape, camera, gap, grid);

        grid.a = 075f;
        GraphicsToolkit.drawGrid(shape, camera, 5 * gap, grid);

        grid.a = 1;
        GraphicsToolkit.drawGrid(shape, camera, 10 * gap, grid);
        GraphicsToolkit.drawAxis(shape, camera);
    }
    
    public static void drawRectangeLine(ShapeRenderer shape, Rectangle rect, float lineThick){
    	shape.rectLine(rect.x, rect.y,  rect.x,  rect.y+rect.height, lineThick);
    	shape.rectLine(rect.x, rect.y+rect.height,  rect.x+rect.width,  rect.y+rect.height, lineThick);
    	shape.rectLine(rect.x+rect.width,  rect.y+rect.height,rect.x+rect.width,  rect.y, lineThick);
    	shape.rectLine(rect.x+rect.width,  rect.y,rect.x, rect.y, lineThick);
    }

    public static void drawGrid(ShapeRenderer shape, OrthographicCamera camera, float gap, Color color) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        if (gap <= 0) gap = 0.001f;
        float x = camera.position.x;
        float y = camera.position.y;
        float w = camera.viewportWidth;
        float h = camera.viewportHeight;
        float z = camera.zoom;

        shape.begin(ShapeType.Line);
        shape.setColor(color);
        for (float d = 0; d < x + w / 2 * z; d += gap)
            shape.line(d, y - h / 2 * z, d, y + h / 2 * z);
        for (float d = -gap; d > x - w / 2 * z; d -= gap)
            shape.line(d, y - h / 2 * z, d, y + h / 2 * z);
        for (float d = 0; d < y + h / 2 * z; d += gap)
            shape.line(x - w / 2 * z, d, x + w / 2 * z, d);
        for (float d = -gap; d > y - h / 2 * z; d -= gap)
            shape.line(x - w / 2 * z, d, x + w / 2 * z, d);
        shape.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public static void drawAxis(ShapeRenderer shape, OrthographicCamera camera) {
        float x = camera.position.x;
        float y = camera.position.y;
        float w = camera.viewportWidth;
        float h = camera.viewportHeight;
        float z = camera.zoom;

        shape.begin(ShapeType.Line);
        shape.setColor(xAxis);
        shape.line(0, y - h / 2 * z, 0, y + h / 2 * z);

        shape.setColor(yAxis);
        shape.line(x - w / 2 * z, 0, x + w / 2 * z, 0);
        shape.end();
    }
}
