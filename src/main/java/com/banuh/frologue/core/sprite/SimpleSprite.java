package com.banuh.frologue.core.sprite;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SimpleSprite extends Sprite {

    public SimpleSprite(String src, String name) {
        super(src, name);
        width = (int)img.getWidth();
        height = (int)img.getHeight();
    }

    public SimpleSprite(Image img, String name) {
        super(img, name);
        width = (int)img.getWidth();
        height = (int)img.getHeight();
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double scale) {
        draw(gc, x, y, scale, false);
    }

    public void draw(GraphicsContext gc, double x, double y, double scale, boolean flip) {
        if (flip) {
            gc.save();
            gc.translate(x + width * scale, y);
            gc.scale(-1, 1);
            gc.drawImage(img, 0, 0, width * scale, height * scale);
            gc.restore();
        } else {
            gc.drawImage(img, x, y, width * scale, height * scale);
        }
    }

    @Override
    public Sprite clone() {
      return new SimpleSprite(img, name);
    }
}
