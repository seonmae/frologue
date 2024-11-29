package com.banuh.frologue.core.sprite;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Sprite {
    public Image img;
    public String name;
    public int width;
    public int height;

    public Sprite(String src, String name) {
        this.img = new Image(src);
        this.name = name;
    }

    public Sprite(Image img, String name) {
        this.img = img;
        this.name = name;
    }

    public abstract void draw(GraphicsContext gc, double x, double y, double scale);
    public abstract void draw(GraphicsContext gc, double x, double y, double scale, boolean flip);

    public abstract Sprite clone();
}