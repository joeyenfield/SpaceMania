package com.emptypockets.spacemania.gui.tools;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class StageScreen extends GameScreen {

    Viewport stageViewport;
    private Stage stage;

    public StageScreen(InputMultiplexer inputMultiplexer) {
        super(inputMultiplexer);
        stageViewport = new ExtendViewport(800, 600);
    }

    public abstract void createStage(Stage stage);

    @Override
    public void addInputMultiplexer(InputMultiplexer input) {
        super.addInputMultiplexer(input);
    }

    @Override
    public void removeInputMultiplexer(InputMultiplexer input) {
        super.removeInputMultiplexer(input);
        input.removeProcessor(getStage());
    }

    @Override
    public void show() {
        super.show();
        Stage stage = new Stage(stageViewport);
        setStage(stage);
        super.show();
        createStage(getStage());
    }

    @Override
    public void hide() {
        super.hide();
        if (getStage() != null) {
            getStage().dispose();
        }
        setStage(null);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stageViewport.update(width, height, true);

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
            if (getStage().getBatch().isDrawing()) {
                //Bug where draw fails which results in the batch.end never being called
                getStage().getBatch().end();
            }
            //Console.error(e);
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        if (this.stage != null) {
            parentInputMultiplexer.removeProcessor(this.stage);
        }
        this.stage = stage;
        if (this.stage != null) {
            parentInputMultiplexer.addProcessor(0, this.stage);
        }
    }


}
