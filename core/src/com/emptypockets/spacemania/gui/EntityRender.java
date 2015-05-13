package com.emptypockets.spacemania.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.entities.BaseEntity;
import com.emptypockets.spacemania.engine.entityManager.EntityManager;
import com.emptypockets.spacemania.engine.entityManager.processors.EntityProcessor;

/**
 * Created by jenfield on 10/05/2015.
 */
public class EntityRender implements EntityProcessor<BaseEntity> {

    ShapeRenderer shapeRender;
    boolean debugEnabled = true;

    public EntityRender() {
        shapeRender = new ShapeRenderer();
    }

    public void renderDebug(OrthographicCamera camera, EntityManager manager) {
        shapeRender.setProjectionMatrix(camera.combined);
        if (manager == null) {
            return;
        }

        shapeRender.begin(ShapeRenderer.ShapeType.Line);
        manager.processEntities(this);
        shapeRender.end();


    }

    public void render(OrthographicCamera camera, GameEngine engine) {
        if (debugEnabled) {
            renderDebug(camera, engine.getEntityManager());
        }
    }

    @Override
    public void processEntity(BaseEntity entity) {
        shapeRender.setColor(Color.RED);
        shapeRender.circle(entity.getPos().x, entity.getPos().y, 3f);

        shapeRender.setColor(Color.GREEN);
        //shapeRender.line(ent.getPos().x, ent.getPos().y, ent.getPos().x + ent.getVel().x, ent.getPos().y + ent.getVel().y);

        shapeRender.setColor(Color.BLUE);
        // shapeRender.line(ent.getPos().x, ent.getPos().y, ent.getPos().x + ent.getAcl().x, ent.getPos().y + ent.getAcl().y);

        shapeRender.setColor(Color.WHITE);
        // shapeRender.polygon(ent.getBounds().getTransformedVertices());
    }
}
