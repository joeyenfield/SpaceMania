package com.emptypockets.spacemania.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.emptypockets.spacemania.EntityRender;
import com.emptypockets.spacemania.command.CommandLinePanel;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.engine.BaseEntity;
import com.emptypockets.spacemania.engine.entityManager.BoundedEntityManager;
import com.emptypockets.spacemania.gui.tools.StageScreen;
import com.emptypockets.spacemania.network.client.ClientManager;

import com.emptypockets.spacemania.utils.GraphicsToolkit;
import com.emptypockets.spacemania.utils.OrthoCamController;

public class ClientScreen extends StageScreen {
    int minTouchSize = 60;
    int insetSize = 10;
    int touchPadSize = 200;

    CommandLinePanel commandLinePanel;
    Touchpad touchPad;
    ClientManager client;

    ShapeRenderer shape;
    OrthoCamController control;

    TextButton showConsole;
    boolean alive;

    BoundedEntityManager<BaseEntity> manager;
    EntityRender render;

    Rectangle bounds;


    public ClientScreen(InputMultiplexer inputMultiplexer) {
        super(inputMultiplexer);
        client = new ClientManager();
        setClearColor(Color.BLACK);
        control = new OrthoCamController(getScreenCamera());
    }

    @Override
    public void addInputMultiplexer(InputMultiplexer input) {
        super.addInputMultiplexer(input);
        input.addProcessor(control);
    }

    @Override
    public void removeInputMultiplexer(InputMultiplexer input) {
        super.removeInputMultiplexer(input);
        input.removeProcessor(control);
    }

    @Override
    public void show() {
        super.show();
        shape = new ShapeRenderer();

        manager = new BoundedEntityManager<BaseEntity>();
        for (int i = 0; i < 100; i++) {
            BaseEntity entity = new BaseEntity();
            entity.getPos().set(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));
            entity.getVel().set(MathUtils.random(-50, 50), MathUtils.random(-50, 50));
            entity.setSize(MathUtils.random(50), MathUtils.random(50));
            entity.setAngVel(MathUtils.random(-90, 90));
            manager.addEntity(entity);
        }
        bounds = new Rectangle(0, 0, 500, 500);
        manager.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
        render = new EntityRender();

        Console.println("AWSOMEO - Showing ");
    }

    @Override
    public void hide() {
        super.hide();
        if (shape != null) {
            shape.dispose();
        }
        shape = null;

        if (commandLinePanel != null) {
            commandLinePanel.dispose();
        }
        commandLinePanel = null;

        if(client != null){
            client.dispose();
        }
        client = null;
        Console.println("AWSOMEO - Hideing");
    }


    @Override
    public void createStage(Stage stage) {
        showConsole = new TextButton("C", getSkin());
        touchPad = new Touchpad(0, getSkin());

        Pixmap pix = new Pixmap(4, 4, Format.RGBA8888);
        Color c = new Color(Color.LIGHT_GRAY);
        c.a = 0.2f;
        pix.setColor(c);
        pix.fill();
        pix.setColor(Color.LIGHT_GRAY);
        pix.drawRectangle(0, 0, 4, 4);

        NinePatch ninePatch = new NinePatch(new Texture(pix), 1, 1, 1, 1);
        Drawable draw = new NinePatchDrawable(ninePatch);
        touchPad.getStyle().background = draw;

        Table layout = new Table();
        // top
        layout.row();
        layout.add();
        layout.add().fillX().expandX();
        layout.add();

        // middle
        layout.row();
        layout.add().fillY().expandY();
        layout.add().fill().expand();
        layout.add().fillY().expandY();

        // bottom
        layout.row();
        layout.add(touchPad).width(touchPadSize).size(touchPadSize);
        layout.add().fillX().expandX();
        layout.add();
        layout.setFillParent(true);

        Table inset = new Table();
        inset.row();
        inset.add();
        inset.add().height(insetSize).expandX().fillX();
        inset.add(showConsole).size(minTouchSize, minTouchSize);

        inset.row();
        inset.add().width(insetSize).expandY().fillY();
        inset.add(layout).fill().expand();
        inset.add().width(insetSize).expandY().fillY();

        inset.row();
        inset.add();
        inset.add().height(insetSize).expandX().fillX();
        inset.add();

        inset.setFillParent(true);

        stage.addActor(inset);

        commandLinePanel = new CommandLinePanel(client.getCommand(), minTouchSize);
        stage.addActor(commandLinePanel);
        commandLinePanel.setVisible(false);


        showConsole.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                commandLinePanel.setVisible(!commandLinePanel.isVisible());
                updateCommandScreenSize();
            }
        });
    }

    public ClientManager getClient() {
        return client;
    }

    public void updateCommandScreenSize() {
        commandLinePanel.setPosition(0, 0);
        commandLinePanel.setSize(getStage().getWidth(), getStage().getHeight() - minTouchSize);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        updateCommandScreenSize();
    }

    @Override
    public void initializeRender() {
        super.initializeRender();
        shape.setProjectionMatrix(getScreenCamera().combined);
    }

    @Override
    public void drawBackground(float delta) {
        GraphicsToolkit.draw2DAxis(shape, getScreenCamera(), 100, Color.WHITE);
    }

    @Override
    public void drawScreen(float delta) {
        manager.update(Gdx.graphics.getRawDeltaTime());
        render.render(getScreenCamera(), manager);
    }

    @Override
    public void drawOverlay(float delta) {
    }

    @Override
    public void updateLogic(float delta) {
        super.updateLogic(delta);
    }

}
