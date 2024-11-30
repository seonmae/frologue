package com.banuh.frologue.core.camera;

import com.banuh.frologue.core.utils.Vector2D;

public class GameCamera {
    public Vector2D pos;
    public double scale;

    public GameCamera(double x, double y, double scale) {
        pos = new Vector2D(x, y);
        this.scale = scale;
    }
}
