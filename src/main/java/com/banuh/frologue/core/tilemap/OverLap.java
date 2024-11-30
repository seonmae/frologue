package com.banuh.frologue.core.tilemap;
import com.banuh.frologue.core.utils.Vector2D;

public class OverLap {
    public boolean is;
    public Vector2D tilePos;
    public double width;
    public double height;
    public String direction;

    public OverLap(boolean is, Vector2D tilePos, double width, double height, String direction) {
        this.is = is;
        this.tilePos = tilePos;
        this.width = width;
        this.height = height;
        this.direction = direction;
    }

    public OverLap(boolean is) {
        this.is = is;
    }
}