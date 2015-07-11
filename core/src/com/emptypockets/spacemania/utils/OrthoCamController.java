package com.emptypockets.spacemania.utils;

import java.util.HashMap;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class OrthoCamController implements InputProcessor {
    final OrthographicCamera camera;
    final Vector3 curr = new Vector3();
    final Vector3 last = new Vector3(-1, -1, -1);
    final Vector3 lastMouse = new Vector3(-1, -1, -1);
    final Vector3 delta = new Vector3();
    Vector2 v1 = new Vector2();
    Vector2 v2 = new Vector2();
    float distance;
    int finger_one_pointer = -1;
    int finger_two_pointer = -1;
    float initialDistance = 0f;
    boolean zoomEnabled = true;
    boolean limitZoom = true;
    float maxZoom = 10;
    float minZoom = 1 / 10f;
    private HashMap<Integer, Vector2> pointers;

    public OrthoCamController(OrthographicCamera camera) {
        this.camera = camera;
        this.pointers = new HashMap<Integer, Vector2>();
        distance = 0f;
    }


    @Override
    public synchronized boolean touchDown(int x, int y, int pointer, int button) {
        if (pointers.size() == 0) {
            // no fingers down so assign v1
            finger_one_pointer = pointer;
            v1 = new Vector2(x, y);
            pointers.put(pointer, v1);
        } else if (pointers.size() == 1) {
            //Clear Last position
            last.set(-1, -1, -1);
            // figure out which finger is down
            if (finger_one_pointer == -1) {
                // finger two is still down
                finger_one_pointer = pointer;
                v1 = new Vector2(x, y);
                pointers.put(pointer, v1);
                initialDistance = v1.dst(pointers.get(finger_two_pointer));
                initialDistance *= camera.zoom;
            } else if (finger_two_pointer == -1) {
                // finger one is still down
                finger_two_pointer = pointer;
                v2 = new Vector2(x, y);
                pointers.put(pointer, v2);
                initialDistance = v2.dst(pointers.get(finger_one_pointer));
                initialDistance *= camera.zoom;
            }
        }
        return true;
    }

    @Override
    public synchronized boolean touchDragged(int x, int y, int pointer) {
        if (pointers.size() < 2) {
            // one finger (drag)
            camera.unproject(curr.set(x, y, 0));
            if (!(last.x == -1 && last.y == -1 && last.z == -1)) {
                camera.unproject(delta.set(last.x, last.y, 0));
                delta.sub(curr);
                camera.position.add(delta.x, delta.y, 0);
            }
            last.set(x, y, 0);
        } else {
            // two finger pinch (zoom)
            // now fingers are being dragged so measure the distance and apply
            // zoom
            if (pointer == finger_one_pointer) {
                v1 = new Vector2(x, y);
                v2 = pointers.get(finger_two_pointer);
                pointers.put(pointer, v1);
            } else if (pointer == finger_two_pointer) {
                v2 = new Vector2(x, y);
                v1 = pointers.get(finger_one_pointer);
                pointers.put(pointer, v2);
            } else {
                v1 = null;
                v2 = null;
            }
            if (v2 != null && v1 != null) {
                float mx = (v2.x + v1.x) / 2;
                float my = (v2.y + v1.y) / 2;
                distance = v2.dst(v1);
                float scaleRatio = initialDistance / distance;
                changeZoom(scaleRatio, mx, my);
            }
        }
        return true;
    }

    @Override
    public synchronized boolean touchUp(int x, int y, int pointer, int button) {
        if (pointer == finger_one_pointer) {
            finger_one_pointer = -1;
            pointers.remove(pointer);
        } else if (pointer == finger_two_pointer) {
            finger_two_pointer = -1;
            pointers.remove(pointer);
        }

        last.set(-1, -1, -1);
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
    	return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        lastMouse.x = x;
        lastMouse.y = y;
        lastMouse.z = 0;
        return true;
    }

    public void changeZoom(float zoom, float x, float y) {
        //Zoom Disabled
        if (!zoomEnabled) {
            return;
        }
        //Zoom Limited
        if (limitZoom && (zoom > maxZoom || zoom < minZoom)) {
            return;
        }
        Vector3 before = new Vector3(x, y, 0);
        camera.unproject(before);

        camera.zoom = zoom;
        camera.update();
        Vector3 after = new Vector3(x, y, 0);
        camera.unproject(after);

        camera.translate(before.x - after.x, before.y - after.y, 0);
    }

    public void setLimitZoom(boolean limit) {
        limitZoom = limit;
    }

    @Override
    public boolean scrolled(int amount) {
        float newZoom = camera.zoom * (1 + (amount < 0 ? 0.1f : -0.1f));
        changeZoom(newZoom, lastMouse.x, lastMouse.y);
        return true;
    }

    public boolean isZoomEnabled() {
        return zoomEnabled;
    }

    public void setZoomEnabled(boolean zoomEnabled) {
        this.zoomEnabled = zoomEnabled;
    }


    public float getMaxZoom() {
        return maxZoom;
    }


    public void setMaxZoom(float maxZoom) {
        this.maxZoom = maxZoom;
    }


    public float getMinZoom() {
        return minZoom;
    }


    public void setMinZoom(float minZoom) {
        this.minZoom = minZoom;
    }

}