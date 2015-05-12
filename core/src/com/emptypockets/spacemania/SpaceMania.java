package com.emptypockets.spacemania;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.engine.BaseEntity;
import com.emptypockets.spacemania.engine.entityManager.BoundedEntityManager;
import com.emptypockets.spacemania.gui.ClientScreen;
import com.emptypockets.spacemania.gui.tools.Scene2DToolkit;

public class SpaceMania extends Game {

    InputMultiplexer input;
    public ClientScreen screen;

    @Override
    public void create() {
        input = new InputMultiplexer();
        Gdx.input.setInputProcessor(input);
        screen = new ClientScreen(input);
        setScreen(screen);


    }

    @Override
    public void dispose() {
        super.dispose();
        Gdx.input.setInputProcessor(null);
        setScreen(null);
        if(screen != null) {
            screen.dispose();
        }
        screen = null;
        Scene2DToolkit.getToolkit().disposeSkin();
    }

    @Override
    public void resume() {
        super.resume();
//        create();
    }

    @Override
    public void pause() {
        super.pause();
//        dispose();
    }
}
