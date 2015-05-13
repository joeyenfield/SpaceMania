package com.emptypockets.spacemania.engine.entities;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by jenfield on 10/05/2015.
 */
public class BaseEntity {
    static int count = 0;
    int id = count++;

    Vector2 pos;
    Vector2 vel;
    Vector2 acl;

    float ang;
    float angVel;
    float angAcl;

    Polygon bounds;

    public BaseEntity() {
        pos = new Vector2();
        vel = new Vector2();
        acl = new Vector2();
        bounds = new Polygon();
        bounds.setVertices(new float[]{
                -1, -1,
                1, -1,
                1, 1,
                -1, 1});
    }

    public void setSize(int sizeX, int sizeY) {
        bounds.setVertices(new float[]{
                -sizeX, -sizeY,
                sizeX, -sizeY,
                sizeX, sizeY,
                -sizeX, sizeY});
    }

    public void update(float timeDelta) {
        vel.x += acl.x * timeDelta;
        vel.y += acl.y * timeDelta;

        pos.x += vel.x * timeDelta;
        pos.y += vel.y * timeDelta;

        angVel += angAcl * timeDelta;
        ang += angVel * timeDelta;

        bounds.setPosition(pos.x, pos.y);
        bounds.setRotation(ang);
    }

    public Vector2 getVel() {
        return vel;
    }

    public void setVel(Vector2 vel) {
        this.vel = vel;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public Vector2 getAcl() {
        return acl;
    }

    public void setAcl(Vector2 acl) {
        this.acl = acl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAng() {
        return ang;
    }

    public void setAng(float ang) {
        this.ang = ang;
    }

    public float getAngVel() {
        return angVel;
    }

    public void setAngVel(float angVel) {
        this.angVel = angVel;
    }

    public float getAngAcl() {
        return angAcl;
    }

    public void setAngAcl(float angAcl) {
        this.angAcl = angAcl;
    }

    public Polygon getBounds() {
        return bounds;
    }

    public void setBounds(Polygon bounds) {
        this.bounds = bounds;
    }

    public boolean isDead() {
        return false;
    }
}
