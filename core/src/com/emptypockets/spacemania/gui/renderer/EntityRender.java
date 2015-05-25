package com.emptypockets.spacemania.gui.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.engine.Engine;
import com.emptypockets.spacemania.network.engine.EntityManager;
import com.emptypockets.spacemania.network.engine.entities.Entity;

/**
 * Created by jenfield on 10/05/2015.
 */
public class EntityRender implements SingleProcessor<Entity> {

    ShapeRenderer shapeRender;
    boolean debugEnabled = true;

    float velScale = .5f;
    float aclScale = .5f;
    
    public EntityRender() {
        shapeRender = new ShapeRenderer();
    }

    public void renderDebug(OrthographicCamera camera, EntityManager manager) {
        shapeRender.setProjectionMatrix(camera.combined);
        if (manager == null) {
            return;
        }
        shapeRender.begin(ShapeRenderer.ShapeType.Line);
        manager.process(this);
        shapeRender.end();


    }

    public void render(OrthographicCamera camera, Engine engine) {
        if (debugEnabled) {
            renderDebug(camera, engine.getEntityManager());
        }
    }

    @Override
    public void process(Entity entity) {
        shapeRender.setColor(Color.RED);
        shapeRender.circle(entity.getState().getPos().x, entity.getState().getPos().y, 3f);

        shapeRender.setColor(Color.GREEN);
        shapeRender.line(entity.getPos().x, entity.getPos().y, entity.getPos().x + entity.getVel().x*velScale, entity.getPos().y + entity.getVel().y*velScale);

        shapeRender.setColor(Color.BLUE);
         shapeRender.line(entity.getPos().x, entity.getPos().y, entity.getPos().x + entity.getAcl().x*aclScale, entity.getPos().y + entity.getAcl().y*aclScale);

        shapeRender.setColor(Color.WHITE);
        // shapeRender.polygon(ent.getBounds().getTransformedVertices());
    }
}
