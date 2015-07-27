package com.emptypockets.spacemania.metrics.plotter;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.emptypockets.spacemania.gui.renderer.TextRender;
import com.emptypockets.spacemania.metrics.events.EventSystem;
import com.emptypockets.spacemania.metrics.events.render.EventRender;
import com.emptypockets.spacemania.metrics.plotter.data.TimeSeriesDataset;
import com.emptypockets.spacemania.metrics.plotter.graphs.line.LinePlotDataGraph;
import com.emptypockets.spacemania.metrics.plotter.graphs.line.LinePlotDescription;
import com.emptypockets.spacemania.utils.CameraHelper;
import com.emptypockets.spacemania.utils.GraphicsToolkit;
import com.emptypockets.spacemania.utils.OrthoCamController;
import com.emptypockets.spacemania.utils.RandomString;

public class PlotterViewer extends ApplicationAdapter implements GestureListener {
	ShapeRenderer shape;
	OrthographicCamera camera;
	OrthoCamController cameraControl;

	Color gridColor = new Color(Color.LIGHT_GRAY);
	Color markColor = new Color(Color.WHITE);
	float markSize = 1;
	ArrayList<LinePlotDataGraph> graphs = new ArrayList<LinePlotDataGraph>();
	SpriteBatch sprite;
	BitmapFont font;

	TimeSeriesDataset range;

	Vector3 mapPos = new Vector3();
	Vector2[] touchData = new Vector2[] { new Vector2(), new Vector2() };
	Vector2[] touchGraphData = new Vector2[] { new Vector2(), new Vector2() };

	Stage stage;
	DataLoggerGraphManager manager;
	TextRender textRender;

	CameraHelper helper;

	@Override
	public void create() {
		helper = new CameraHelper();
		textRender = new TextRender();
		stage = new Stage(new ScreenViewport());

		shape = new ShapeRenderer();
		sprite = new SpriteBatch();
		font = new BitmapFont();
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		camera = new OrthographicCamera();
		cameraControl = new OrthoCamController(camera);

		InputMultiplexer input = new InputMultiplexer();
		input.addProcessor(stage);
		input.addProcessor(new GestureDetector(this));
		input.addProcessor(cameraControl);

		Gdx.input.setInputProcessor(input);

		gridColor.a = 0.1f;
		range = DataLogger.read("server-update");
		int sizeX = 10000;
		int dataGraphHeight = 500;
		float ticksGraphHeight = 20;

		int graphGap = 10;

		int graphCount = 0;

		LinePlotDataGraph customGraph = createLineGraph(0, (graphCount++) * (dataGraphHeight + graphGap), sizeX, dataGraphHeight, "Offsets", (Color) null, "client-ent-1-off-x", "ent-offset-server-x");
		graphs.add(customGraph);
		graphs.add(createLineGraph(0, (graphCount++) * (dataGraphHeight + graphGap), sizeX, dataGraphHeight, "Offsets", (Color) null, "client-ent-1-off-x", "ent-offset-server-x"));
		graphs.add(createLineGraph(0, (graphCount++) * (dataGraphHeight + graphGap), sizeX, dataGraphHeight, "Pos Stuff", (Color) null, "server-ent-1-pos-x", "client-ent-1-pos-x"));
		graphs.add(createLineGraph(0, (graphCount++) * (dataGraphHeight + graphGap), sizeX, dataGraphHeight, "Vel", (Color) null, "server-ent-1-vel-x", "client-ent-1-vel-x"));
		graphs.add(createLineGraph(0, (graphCount++) * (dataGraphHeight + graphGap), sizeX, dataGraphHeight, "Vel", (Color) null, "tmp-x", "client-ent-1-vel-x"));

		graphs.add(createLineGraph(0, (graphCount++) * (dataGraphHeight + graphGap), sizeX, dataGraphHeight, "client-input-x", getRandomColor()));
		graphs.add(createLineGraph(0, (graphCount++) * (dataGraphHeight + graphGap), sizeX, dataGraphHeight, "server-input-x", getRandomColor()));

		graphCount++;
		float allDataHeight = 100;
		int count = 0;
		ArrayList<String> names = new ArrayList<String>(DataLogger.getData());
		Collections.sort(names);
		for (String name : names) {
			int lastHeight = (graphCount * (dataGraphHeight + graphGap));
			lastHeight += count++ * (allDataHeight + graphGap);
			graphs.add(createLineGraph(0, lastHeight, sizeX, allDataHeight, name, getRandomColor()));
		}

		int ticks = 0;
		ticks++;

		graphs.add(createSpikeGraph(0, (ticks++) * (-(ticksGraphHeight + graphGap)), sizeX, ticksGraphHeight, "client-logic", getRandomColor()));
		graphs.add(createSpikeGraph(0, (ticks++) * (-(ticksGraphHeight + graphGap)), sizeX, ticksGraphHeight, "server-update", getRandomColor()));
		graphs.add(createSpikeGraph(0, (ticks++) * (-(ticksGraphHeight + graphGap)), sizeX, ticksGraphHeight, "client-sync", getRandomColor()));
		graphs.add(createSpikeGraph(0, (ticks++) * (-(ticksGraphHeight + graphGap)), sizeX, ticksGraphHeight, "server-sync", getRandomColor()));

		manager = new DataLoggerGraphManager(stage, "Test", customGraph);
		stage.addActor(manager);
		manager.setVisible(true);
	}

	public LinePlotDataGraph createSpikeGraph(float x, float y, float sizeX, float sizeY, String name, Color color) {
		return createDataGraph(x, y, sizeX, sizeY, name, color, color, false, false, true, name);
	}

	public LinePlotDataGraph createLineGraph(float x, float y, float sizeX, float sizeY, String name, Color color) {
		return createDataGraph(x, y, sizeX, sizeY, name, color, color, true, true, false, name);
	}

	public LinePlotDataGraph createLineGraph(float x, float y, float sizeX, float sizeY, String name, Color color, String... names) {
		return createDataGraph(x, y, sizeX, sizeY, name, color, color, true, true, false, names);
	}

	private LinePlotDataGraph createDataGraph(float x, float y, float sizeX, float sizeY, String name, Color pointColor, Color lineColor, boolean drawLine, boolean drawPoint, boolean drawSpike, String... names) {
		LinePlotDescription[] descs = new LinePlotDescription[names.length];

		for (int i = 0; i < descs.length; i++) {
			LinePlotDescription desc = new LinePlotDescription();
			desc.setName(names[i]);
			if (pointColor == null) {
				desc.setPointColor(getRandomColor());
			} else {
				desc.setPointColor(pointColor);
			}
			if (lineColor == null) {
				desc.setLineColor(getRandomColor());
			} else {
				desc.setLineColor(lineColor);
			}
			desc.setSpike(drawSpike);
			desc.setDrawLine(drawLine);
			desc.setDrawPoint(drawPoint);
			descs[i] = desc;
		}
		return createGraph(x, y, sizeX, sizeY, name, descs);
	}

	public Color getRandomColor() {
		return new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);
	}

	private LinePlotDataGraph createGraph(float x, float y, float sizeX, float sizeY, String name, LinePlotDescription... dataset) {
		LinePlotDataGraph graph = new LinePlotDataGraph(name);
		boolean spike = false;
		graph.getScreenBounds().set(x, y, sizeX, sizeY);
		for (int i = 0; i < dataset.length; i++) {
			LinePlotDescription desc = dataset[i];
			TimeSeriesDataset data = DataLogger.read(desc.getName());
			graph.addLineDataset(data, desc);
			spike = spike || desc.isSpike();
		}
		graph.fitRange();
		if (spike) {
			graph.setyMin(0);
		}
		graph.setxMin(range.getMinTime());
		graph.setxMax(range.getMaxTime());
		graph.updateLayout();
		return graph;
	}

	@Override
	public void render() {
		stage.act();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		shape.setProjectionMatrix(camera.combined);
		sprite.setProjectionMatrix(camera.combined);

		Rectangle bounds = new Rectangle();
		helper.getBounds(camera, bounds);

		GraphicsToolkit.drawAxis(shape, camera);
		GraphicsToolkit.drawGrid(shape, camera, 10, gridColor);
		GraphicsToolkit.drawGrid(shape, camera, 50, gridColor);
		GraphicsToolkit.drawGrid(shape, camera, 100, gridColor);

		for (LinePlotDataGraph graph : graphs) {
			graph.drawLine(shape);
		}

		boolean touchValid = false;
		boolean shown[] = new boolean[] { false, false };
		for (int i = 0; i < touchData.length; i++) {
			Vector2 touchPos = touchData[i];
			Vector2 graphData = touchGraphData[i];
			if (touchPos.dst2(0, 0) > 2) {
				shown[i] = true;
				touchValid = true;
				float size;
				mapPos.x = markSize;
				mapPos.y = 0;
				mapPos.z = 0;
				camera.unproject(mapPos);
				size = mapPos.x;

				mapPos.x = 0;
				mapPos.y = 0;
				mapPos.z = 0;
				camera.unproject(mapPos);

				size = Math.abs(size - mapPos.x);

				markColor.a = 0.3f;
				shape.begin(ShapeType.Filled);
				shape.setColor(markColor);
				int src = GL20.GL_SRC_ALPHA;
				int dst = GL20.GL_ONE;
				Gdx.gl20.glEnable(GL20.GL_BLEND);
				Gdx.gl20.glBlendFunc(src, dst);
				shape.rectLine(-1e10f, touchPos.y, 1e10f, touchPos.y, size);
				shape.rectLine(touchPos.x, -1e10f, touchPos.x, 1e10f, size);
				shape.end();

				sprite.begin();
				sprite.enableBlending();
				for (LinePlotDataGraph graph : graphs) {
					String message;
					if (touchValid && graph.getScreenBounds().contains(touchPos.x, touchPos.y)) {
						graphData.x = graph.screenToGraphX(touchPos.x);
						graphData.y = graph.screenToGraphY(touchPos.y);
						message = graph.getName() + " [ " + graphData.x + " - " + graphData.y + "]";
						font.draw(sprite, message, touchPos.x, touchPos.y);
					}
				}

				sprite.end();
			}

			long peroid = 4000;
			float size = 10 + 100 * MathUtils.sin(3.14159f * ((System.currentTimeMillis() % peroid) / (float) peroid));
			size = 100;

			Vector2 pos = new Vector2();
			String[] data = new String[] { "The Quick brown fox jumped over the lazy dog.", "abcdefghijklmnopqrstuvwxyz", "AB\tCDEFGHIJKLMNOPQRSTUVWXYZ", "0123456789.-:?", RandomString.random(10), "FPS : " + Gdx.graphics.getFramesPerSecond() };
			pos.set(size, size);
			shape.begin(ShapeType.Filled);
			shape.setColor(Color.RED);
			for (String text : data) {
				pos.y += 1.5 * size;
				textRender.render(shape, text, pos, size, bounds);
			}
			textRender.render(shape, "S", Vector2.Zero, 100, bounds);
			shape.end();
		}

		if (shown[0] && shown[1]) {
			sprite.begin();
			sprite.enableBlending();
			float dx = Math.abs(touchGraphData[0].x - touchGraphData[1].x);
			float dy = Math.abs(touchGraphData[0].y - touchGraphData[1].y);
			String message = " [ " + dx + " - " + dy + "]";

			float x = Math.min(touchData[0].x, touchData[1].x);
			float y = Math.max(touchData[0].y, touchData[1].y);

			font.draw(sprite, message, x, y + 50);
			sprite.end();
		}
		sprite.begin();
		sprite.enableBlending();
		for (LinePlotDataGraph graph : graphs) {
			String message = graph.getName();
			font.draw(sprite, message, graph.getScreenBounds().x, graph.getScreenBounds().y + graph.getScreenBounds().height);
		}
		sprite.end();

		
		stage.draw();

		
	}

	public static String randomString(int length) {
		StringBuilder builder = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			builder.append((char) MathUtils.random(Character.MAX_VALUE));
		}

		return builder.toString();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();

		stage.getViewport().update(width, height, true);

		manager.setBounds(0, height, 500, 10);

	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		stage.setScrollFocus(null);
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {

		mapPos.x = x;
		mapPos.y = y;
		mapPos.z = 0;
		camera.unproject(mapPos);

		int value = 0;
		if (button != 0) {
			value = 1;
		}
		touchData[value].x = mapPos.x;
		touchData[value].y = mapPos.y;
		if (count > 1) {
			touchData[value].x = 0;
			touchData[value].y = 0;
			selectGraph(x, y);
		}

		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		System.out.println(x + " - " + y);
		return selectGraph(x, y);
	}

	public boolean selectGraph(float x, float y) {
		mapPos.x = x;
		mapPos.y = y;
		mapPos.z = 0;
		camera.unproject(mapPos);

		for (LinePlotDataGraph graph : graphs) {
			if (graph.getScreenBounds().contains(mapPos.x, mapPos.y)) {
				manager.setGraph(graph);
			}
		}
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

}
