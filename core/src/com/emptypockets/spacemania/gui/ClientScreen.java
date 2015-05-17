package com.emptypockets.spacemania.gui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.emptypockets.spacemania.commandLine.CommandLinePanel;
import com.emptypockets.spacemania.gui.tools.StageScreen;
import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.utils.GraphicsToolkit;
import com.emptypockets.spacemania.utils.OrthoCamController;

public class ClientScreen extends StageScreen {
    int minTouchSize = 60;
    int insetSize = 10;
    int touchPadSize = 200;

    CommandLinePanel commandLinePanel;
    Touchpad movePad;
    Touchpad shootPad;
    ClientManager client;

    ShapeRenderer shape;
    OrthoCamController control;

    TextButton showConsole;
    boolean alive;


    EntityRender render;


    public ClientScreen(InputMultiplexer inputMultiplexer) {
        super(inputMultiplexer);
        client = new ClientManager();
        setClearColor(Color.BLACK);
        control = new OrthoCamController(getScreenCamera());

        getClient().getCommand().pushHistory("server setup 10; server start; server status; connect localhost,8080,9090;login jenfield; server startgame");

        getClient().getCommand().pushHistory("connect 192.168.1.100,8080,9090;login user2;");
        getClient().getCommand().pushHistory("connect emptypocketgames.noip.me,8080,9090;login user2;");

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
        render = new EntityRender();
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

        if (client != null) {
            client.dispose();
        }
        client = null;
    }


    @Override
    public void createStage(Stage stage) {
        showConsole = new TextButton("C", getSkin());
        movePad = new Touchpad(0, getSkin());
        shootPad = new Touchpad(0, getSkin());

        Pixmap pix = new Pixmap(4, 4, Format.RGBA8888);
        Color c = new Color(Color.LIGHT_GRAY);
        c.a = 0.2f;
        pix.setColor(c);
        pix.fill();
        pix.setColor(Color.LIGHT_GRAY);
        pix.drawRectangle(0, 0, 4, 4);

        NinePatch ninePatch = new NinePatch(new Texture(pix), 1, 1, 1, 1);
        Drawable draw = new NinePatchDrawable(ninePatch);
        movePad.getStyle().background = draw;
        shootPad.getStyle().background = draw;


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
        layout.add(movePad).width(touchPadSize).size(touchPadSize);
        layout.add().fillX().expandX();
        layout.add(shootPad).width(touchPadSize).size(touchPadSize);

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
//        synchronized (client.getCurrentRoom().getEngine()) {
//            render.render(getScreenCamera(), client.getEngine());
//        }
    }

    @Override
    public void drawOverlay(float delta) {
    }

    @Override
    public void updateLogic(float delta) {
        super.updateLogic(delta);
        //Update Players Data
        if (client.getPlayer() != null) {
//            client.getServerPlayer().getMovement().set(movePad.getKnobPercentX(), movePad.getKnobPercentY());
//            client.getServerPlayer().getShoot().set(shootPad.getKnobPercentX(), shootPad.getKnobPercentY());
        }
        client.update();
    }

}
