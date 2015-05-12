package com.emptypockets.spacemania;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.emptypockets.spacemania.gui.ClientScreen;
import com.emptypockets.spacemania.gui.tools.Scene2DToolkit;

public class SpaceMania extends Game {

    public ClientScreen screen;
    InputMultiplexer input;

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
        if (screen != null) {
            screen.dispose();
        }
        screen = null;
        Scene2DToolkit.getToolkit().disposeSkin();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void pause() {
        super.pause();
    }
}
