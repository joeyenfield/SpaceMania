package com.emptypockets.spacemania;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.emptypockets.spacemania.engine.BaseEntity;
import com.emptypockets.spacemania.engine.entityManager.BaseEntityManager;

import java.util.Collection;

/**
 * Created by jenfield on 10/05/2015.
 */
public class EntityRender {

    ShapeRenderer shapeRender;
    boolean debugEnabled = true;

    public EntityRender() {
        shapeRender = new ShapeRenderer();
    }

    public void renderDebug(OrthographicCamera camera, BaseEntityManager manager) {
        shapeRender.setProjectionMatrix(camera.combined);
        synchronized (manager) {
            Collection<? extends BaseEntity> ents = manager.getEntities();
            shapeRender.begin(ShapeRenderer.ShapeType.Line);
            for (BaseEntity ent : ents) {

                shapeRender.setColor(Color.RED);
                shapeRender.circle(ent.getPos().x, ent.getPos().y, 3f);

                shapeRender.setColor(Color.GREEN);
                shapeRender.line(ent.getPos().x, ent.getPos().y, ent.getPos().x + ent.getVel().x, ent.getPos().y + ent.getVel().y);

                shapeRender.setColor(Color.BLUE);
                shapeRender.line(ent.getPos().x, ent.getPos().y, ent.getPos().x + ent.getAcl().x, ent.getPos().y + ent.getAcl().y);

                shapeRender.setColor(Color.WHITE);
                shapeRender.polygon(ent.getBounds().getTransformedVertices());
            }
            shapeRender.end();
        }
    }

    public void render(OrthographicCamera camera, BaseEntityManager manager) {
        if (debugEnabled) {
            renderDebug(camera,manager);
        }
    }
}
