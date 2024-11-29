package com.banuh.frologue.core.entity;

import com.banuh.frologue.core.utils.Vector2D;

public class Hitbox {
    /** 엔티티 기준 상대위치 */
    private Vector2D pos;
    private int width = 0;
    private int height = 0;

    public Hitbox(double dx, double dy, int width, int height) {
        this.pos = new Vector2D(dx, dy);
        this.width = width;
        this.height = height;
    }

    public Vector2D getPos() {
        return pos;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
