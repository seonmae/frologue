package com.banuh.frologue.core.tilemap;
import com.banuh.frologue.core.utils.Direction;
import com.banuh.frologue.core.utils.Vector2D;

public class OverLap {
    public boolean is;
//    public double width;
//    public double height;
//    public Direction direction;
//    public Vector2D from;
//    public Vector2D to;

    public boolean isLeft = false;
    public boolean isRight = false;
    public boolean isTop = false;
    public boolean isBottom = false;


    public Vector2D topTilePos;
    public Vector2D rightTilePos;
    public Vector2D bottomTilePos;
    public Vector2D leftTilePos;

    public OverLap(boolean is) {
        this.is = is;
    }
}