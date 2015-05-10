package com.emptypockets.spacemania.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GraphicsToolkit {

	public static void draw2DAxis(ShapeRenderer shape, OrthographicCamera camera, float gap, Color color){
		Color major =color;
		major.a = 1;
		
		Color minor = color;
		minor.a = 0.5f;
		
		Color grid = color;
		grid.a = 0.1f;
		
		GraphicsToolkit.drawGrid(shape, camera,gap, grid);
		GraphicsToolkit.drawGrid(shape, camera,5*gap, minor);
		GraphicsToolkit.drawGrid(shape, camera,10*gap, major);
		GraphicsToolkit.drawAxis(shape, camera);
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
        for (float d=0; d<x+w/2*z; d+=gap) shape.line(d, y-h/2*z, d, y+h/2*z);
        for (float d=-gap; d>x-w/2*z; d-=gap) shape.line(d, y-h/2*z, d, y+h/2*z);
        for (float d=0; d<y+h/2*z; d+=gap) shape.line(x-w/2*z, d, x+w/2*z, d);
        for (float d=-gap; d>y-h/2*z; d-=gap) shape.line(x-w/2*z, d, x+w/2*z, d);
        shape.end();
	}
	
	public static void drawAxis(ShapeRenderer shape, OrthographicCamera camera){
		float x = camera.position.x;
        float y = camera.position.y;
        float w = camera.viewportWidth;
        float h = camera.viewportHeight;
        float z = camera.zoom;
        
		Color xAxis = Color.RED;
		xAxis.a = 0.5f;
		
		Color yAxis = Color.BLUE;
		yAxis.a = 0.5f;
		
		shape.begin(ShapeType.Line);
		shape.setColor(xAxis);
		shape.line(0,y-h/2*z, 0, y+h/2*z);
		
		shape.setColor(yAxis);
		shape.line(x-w/2*z, 0, x+w/2*z,0);
		shape.end();
	}
}
