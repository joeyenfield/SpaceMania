package com.emptypockets.spacemania.gui.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.client.ClientEngine;
import com.emptypockets.spacemania.network.engine.grid.GridSystem;
import com.emptypockets.spacemania.network.engine.grid.GridSystemListener;

public class GridTextureRenderer implements GridSystemListener {
	Texture gridTexture;
	String vertexShader;
	String fragmentShader;
	ShaderProgram shaderProgram;
	Mesh mesh;
	Rectangle world = new Rectangle();

	float[] vertData;
	short[] idxData;
	int pixSize = 5;

	int sizeX = 0;
	int sizeY = 0;

	boolean highQuality = true;

	public GridTextureRenderer() {
		init();
	}

	private void createShader() {
		vertexShader = Gdx.files.internal("shaders/vertex.glsl").readString();
		fragmentShader = Gdx.files.internal("shaders/fragment.glsl").readString();
		shaderProgram = new ShaderProgram(vertexShader, fragmentShader);

		gridTexture = new Texture("back.png");
		gridTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}

	public void setData(int x, int y, Vector2 pos, Vector2 uv, float alpha) {
		setData(x, y, pos.x, pos.y, uv.x, uv.y, alpha);
	}

	public void setPos(int x, int y, Vector2 p) {
		int pos = getVertPos(x, y);
		vertData[pos] = p.x;
		vertData[pos + 1] = p.y;
	}

	public void setWeight(int x, int y, float weight) {
		int pos = getVertPos(x, y);
		vertData[pos + 4] = weight;
	}

	public void setData(int x, int y, float pX, float pY, float u, float v, float a) {
		int pos = getVertPos(x, y);
		vertData[pos] = pX;
		vertData[pos + 1] = pY;
		vertData[pos + 2] = u;
		vertData[pos + 3] = v;
		vertData[pos + 4] = a;
	}

	public String getData(int x, int y) {
		int pos = getVertPos(x, y);
		return "P[" + vertData[pos] + "," + vertData[pos + 1] + "] U[" + vertData[pos + 3] + "," + vertData[pos + 4] + "]";
	}

	private int getVertPos(int x, int y) {
		return ((int) getVert(x, y)) * pixSize;
	}

	private int getVert(int x, int y) {
		return (int) (x * sizeY + y);
	}

	public void rebuild(GridSystem system) {
		if (sizeX != system.getSizeX() || sizeY != system.getSizeY()) {
			this.sizeX = system.getSizeX();
			this.sizeY = system.getSizeY();
			init();
		}

		Vector2 pos = new Vector2();
		Vector2 uv = new Vector2();
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				float xf = x / (sizeX - 1f);
				float yf = y / (sizeY - 1f);
				pos.x = system.getSettings().bounds.x + system.getSettings().bounds.width * xf;
				pos.y = system.getSettings().bounds.y + system.getSettings().bounds.height * yf;

				uv.x = (pos.x / gridTexture.getWidth());
				uv.y = (pos.y / gridTexture.getHeight());
				setData(x, y, pos, uv, 1f);
			}
		}
	}

	private void createMesh() {
		vertData = new float[sizeX * sizeY * pixSize];
		Vector2 pos = new Vector2();
		Vector2 uv = new Vector2();
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				float xf = x / (sizeX - 1f);
				float yf = y / (sizeY - 1f);
				pos.x = xf;
				pos.y = yf;
				uv.x = (pos.x / gridTexture.getWidth());
				uv.y = (pos.y / gridTexture.getHeight());
				setData(x, y, pos, uv, 1f);
			}
		}

		idxData = new short[sizeX * sizeY * 6];
		int count = 0;
		for (short x = 0; x < sizeX - 1; x++) {
			for (short y = 0; y < sizeY - 1; y++) {
				idxData[count++] = (short) getVert(x, y);
				idxData[count++] = (short) getVert(x + 1, y + 1);
				idxData[count++] = (short) getVert(x, y + 1);

				idxData[count++] = (short) getVert(x, y);
				idxData[count++] = (short) getVert(x + 1, y);
				idxData[count++] = (short) getVert(x + 1, y + 1);
			}
		}

		if (mesh != null) {
			mesh.dispose();
		}
		// Create a mesh out of two triangles rendered clockwise without indices
		mesh = new Mesh(true, sizeX * sizeY, idxData.length, new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"), new VertexAttribute(
				VertexAttributes.Usage.Generic, 1, "a_alpha"));

		mesh.setVertices(vertData);
		mesh.setIndices(idxData);

	}

	public void init() {
		createShader();
		createMesh();
	}

	public void render(OrthographicCamera camera, ClientEngine engine) {
		if (engine.isDynamicGrid()) {
			for (short x = 0; x < sizeX; x++) {
				for (short y = 0; y < sizeY; y++) {
					setPos(x, y, engine.getGridData().getNodePos(x, y));
					setWeight(x, y, .5f);
				}
				mesh.setVertices(vertData);
			}
		}
		gridTexture.bind();
		shaderProgram.begin();

		Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

		shaderProgram.setUniformMatrix("u_projTrans", camera.combined);
		shaderProgram.setUniformi("u_texture", 0);
		mesh.render(shaderProgram, GL20.GL_TRIANGLES);
		shaderProgram.end();
	}

	@Override
	public void gridChanged(GridSystem grid) {
		rebuild(grid);
		world.set(grid.getSettings().bounds);
	}
}
