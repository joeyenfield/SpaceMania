package com.emptypockets.metrics.plotter;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.emptypockets.metrics.plotter.data.TimeSeriesDataset;
import com.emptypockets.metrics.plotter.graphs.line.LinePlotDataGraph;
import com.emptypockets.metrics.plotter.graphs.line.LinePlotDescription;
import com.emptypockets.spacemania.gui.tools.ShapeRenderUtil;
import com.emptypockets.spacemania.utils.CameraHelper;
import com.emptypockets.spacemania.utils.GraphicsToolkit;
import com.emptypockets.spacemania.utils.camera.PanAndZoomCamController;

public class PlotterViewer extends ApplicationAdapter implements GestureListener, InputProcessor {
	ShapeRenderer shape;
	OrthographicCamera camera;
	PanAndZoomCamController cameraControl;

	Color gridColor = new Color(Color.LIGHT_GRAY);
	Color markColor = new Color(Color.WHITE);
	float markSize = 1;
	ArrayList<LinePlotDataGraph> graphs = new ArrayList<LinePlotDataGraph>();

	TimeSeriesDataset range;

	Vector3 mapPos = new Vector3();
	Vector2[] touchData = new Vector2[] { new Vector2(), new Vector2() };
	Vector2[] touchGraphData = new Vector2[] { new Vector2(), new Vector2() };

	Stage stage;
	DataLoggerGraphManager manager;
	ShapeRenderUtil textRender;

	CameraHelper helper;

	int graphWidthPixels = 5000;
	float mainGraphHeight = 200;
	float generalDataHeight = 200;
	float tickDataGraphHeight = 10;
	
	boolean rebuildOnChange = true;
	int graphGap = 10;

	float gridSize = 100;
	
	@Override
	public void create() {
		helper = new CameraHelper();
		textRender = new ShapeRenderUtil();
		textRender.strictDraw = false;
		stage = new Stage(new ScreenViewport());

		shape = new ShapeRenderer();

		camera = new OrthographicCamera();
		cameraControl = new PanAndZoomCamController(camera);

		InputMultiplexer input = new InputMultiplexer();
		input.addProcessor(stage);
		input.addProcessor(new GestureDetector(this));
		input.addProcessor(cameraControl);
		input.addProcessor(this);

		Gdx.input.setInputProcessor(input);

		gridColor.a = 0.1f;

		manager = new DataLoggerGraphManager(stage, "Test", null);
		stage.addActor(manager);
		manager.setVisible(true);
	}

	public synchronized void rebuild() {
		graphs.clear();
		range = DataLogger.read("HOST-UPDATE");

		int graphCount = 0;

		LinePlotDataGraph customGraph = createLineGraph(0, (graphCount++) * (mainGraphHeight + graphGap), graphWidthPixels, mainGraphHeight, "Graph", (Color) null, "HOST-ent-2-pos-x", "CLIENT-ent-2-pos-x","CLIENT-ent-2-net-pos-x");
		graphs.add(customGraph);
		graphs.add(createLineGraph(0, (graphCount++) * (mainGraphHeight + graphGap), graphWidthPixels, mainGraphHeight, "Graph 1", (Color) null, "HOST-ent-2-vel-x", "CLIENT-ent-2-vel-x"));
		graphs.add(createLineGraph(0, (graphCount++) * (mainGraphHeight + graphGap), graphWidthPixels, mainGraphHeight, "Graph 2", (Color) null, "CLIENT-ent-2-off-x"));

		graphCount++;
		
		int count = 0;
		ArrayList<String> names = new ArrayList<String>(DataLogger.getData());
		Collections.sort(names);
		for (String name : names) {
			float lastHeight = (graphCount * (mainGraphHeight + graphGap));
			lastHeight += count++ * (generalDataHeight + graphGap);
			graphs.add(createLineGraph(0, lastHeight, graphWidthPixels, generalDataHeight, name, getRandomColor()));
		}

		int ticks = 0;
		ticks++;

		graphs.add(createSpikeGraph(0, (ticks++) * (-(tickDataGraphHeight + graphGap)), graphWidthPixels, tickDataGraphHeight, "HOST-UPDATE", getRandomColor()));
		graphs.add(createSpikeGraph(0, (ticks++) * (-(tickDataGraphHeight + graphGap)), graphWidthPixels, tickDataGraphHeight, "CLIENT-UPDATE", getRandomColor()));
		graphs.add(createSpikeGraph(0, (ticks++) * (-(tickDataGraphHeight + graphGap)), graphWidthPixels, tickDataGraphHeight, "HOST-net", getRandomColor()));
		graphs.add(createSpikeGraph(0, (ticks++) * (-(tickDataGraphHeight + graphGap)), graphWidthPixels, tickDataGraphHeight, "CLIENT-net", getRandomColor()));

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
			System.out.println(name + "-MIN:" + data.getMinValue());
			System.out.println(name + "-MAX:" + data.getMaxValue());
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
	public synchronized void render() {
		stage.act();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		shape.setProjectionMatrix(camera.combined);

		Rectangle bounds = new Rectangle();
		helper.getBounds(camera, bounds);

		GraphicsToolkit.drawAxis(shape, camera);
		GraphicsToolkit.drawGrid(shape, camera, gridSize/10, gridColor);
		GraphicsToolkit.drawGrid(shape, camera, gridSize/2, gridColor);
		GraphicsToolkit.drawGrid(shape, camera, gridSize, gridColor);

		for (LinePlotDataGraph graph : graphs) {
			graph.drawLine(shape);
		}

		float helperTextHeight = helper.getScreenToCameraPixelX(camera, 30);

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

				shape.begin(ShapeType.Filled);
				shape.setColor(Color.WHITE);
				for (LinePlotDataGraph graph : graphs) {
					String message;
					if (touchValid && graph.getScreenBounds().contains(touchPos.x, touchPos.y)) {
						graphData.x = graph.screenToGraphX(touchPos.x);
						graphData.y = graph.screenToGraphY(touchPos.y);
						message = graph.getName() + ":" + graphData.x + "-" + graphData.y;
						textRender.render(shape, message, touchPos.x, touchPos.y, helperTextHeight, bounds);
					}
				}

				shape.end();
			}
		}

		shape.begin(ShapeType.Filled);
		shape.setColor(Color.WHITE);
		if (shown[0] && shown[1]) {
			float dx = Math.abs(touchGraphData[0].x - touchGraphData[1].x);
			float dy = Math.abs(touchGraphData[0].y - touchGraphData[1].y);
			String message = "(" + dx + "-" + dy + ")";

			float x = Math.min(touchData[0].x, touchData[1].x);
			float y = Math.max(touchData[0].y, touchData[1].y);

			textRender.render(shape, message, x, y + helperTextHeight, helperTextHeight, bounds);
		}
		for (LinePlotDataGraph graph : graphs) {
			String message = graph.getName();
			textRender.render(shape, message, graph.getScreenBounds().x, graph.getScreenBounds().y + graph.getScreenBounds().height - 10, 10, bounds);
		}
		shape.end();

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

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		boolean change = false;

		if (keycode == Keys.F1) {
			rebuild();
		}
		if (keycode == Keys.F2) {
			rebuildOnChange = !rebuildOnChange;
		}
		
		/**
		 * Pixels Width
		 */
		if (keycode == Keys.Q) {
			graphWidthPixels += 200;
			change = true;
		}
		if (keycode == Keys.A) {
			graphWidthPixels -= 200;
			if (graphWidthPixels < 200) {
				graphWidthPixels = 200;
			}
			change = true;
		}

		/**
		 * Main Graph Height
		 */
		if (keycode == Keys.W) {
			mainGraphHeight += 20;
			change = true;
		}
		if (keycode == Keys.S) {
			mainGraphHeight -= 20;
			if (mainGraphHeight < 20) {
				mainGraphHeight = 20;
			}
			change = true;
		}
		
		/**
		 * Data Graph Height
		 */

		if (keycode == Keys.E) {
			generalDataHeight += 20;
			change = true;
		}
		if (keycode == Keys.D) {
			generalDataHeight -= 20;
			if (generalDataHeight < 20) {
				generalDataHeight = 20;
			}
			change = true;
		}

		
		/**
		 * Tick Graph Height
		 */

		if (keycode == Keys.E) {
			tickDataGraphHeight += 2;
			change = true;
		}
		if (keycode == Keys.D) {
			tickDataGraphHeight -= 2;
			if (tickDataGraphHeight < 2) {
				tickDataGraphHeight = 2;
			}
			change = true;
		}
		
		/**
		 * Grid Size
		 */

		if (keycode == Keys.R) {
			gridSize += 20;
		}
		if (keycode == Keys.F) {
			gridSize -= 20;
			if (gridSize < 20) {
				gridSize = 20;
			}
		}

		if (change && rebuildOnChange) {
			rebuild();
		}

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
