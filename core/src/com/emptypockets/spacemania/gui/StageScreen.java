package com.emptypockets.spacemania.gui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class StageScreen extends GameScreen {

    private Stage stage;
    Viewport stageViewport;

    public StageScreen(InputMultiplexer inputMultiplexer) {
        super(inputMultiplexer);
        stageViewport = new ScreenViewport();
    }

    public abstract void createStage(Stage stage);

    @Override
    public void addInputMultiplexer(InputMultiplexer input) {
        input.addProcessor(getStage());
        super.addInputMultiplexer(input);
    }

    @Override
    public void removeInputMultiplexer(InputMultiplexer input) {
        input.removeProcessor(getStage());
        super.removeInputMultiplexer(input);
    }

    public void show() {
        Stage stage = new Stage(stageViewport);
        setStage(stage);
        super.show();
        createStage(getStage());
    }

    @Override
    public void hide() {
        super.hide();
        getStage().dispose();
        setStage(null);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stageViewport.update(width, height);
    }

    @Override
    public void renderScreen(float delta) {
        super.renderScreen(delta);
        eventLogger.begin("RENDER-stage");
        drawStage(delta);
        eventLogger.end("RENDER-stage");
    }

    @Override
    public void updateLogic(float delta) {
        super.updateLogic(delta);
        eventLogger.begin("LOGIC-Stage");
        getStage().act(delta);
        eventLogger.end("LOGIC-Stage");
    }

    public void drawStage(float delta) {
        try {
            getStage().getViewport().apply();
            getStage().draw();
        } catch (Exception e) {

        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


}
