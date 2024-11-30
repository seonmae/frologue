package com.banuh.frologue.core.sprite;

import com.banuh.frologue.core.camera.GameCamera;
import com.banuh.frologue.core.entity.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public abstract class Sprite {
    public Image img;
    public String name;
    public int width;
    public int height;

    public Sprite(String src, String name) {
        String path = "file:src/main/resources/" + src;
        this.img = new Image(path);
        this.name = name;
    }

    public Sprite(Image img, String name) {
        this.img = img;
        this.name = name;
    }

    public abstract void draw(GraphicsContext gc, double x, double y, GameCamera camera, Entity entity, boolean flip);

    public abstract Sprite clone();
}