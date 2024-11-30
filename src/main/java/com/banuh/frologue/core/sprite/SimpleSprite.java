package com.banuh.frologue.core.sprite;

import com.banuh.frologue.core.camera.GameCamera;
import com.banuh.frologue.core.entity.Entity;
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
    public void draw(GraphicsContext gc, double x, double y, GameCamera camera, Entity entity, boolean flip) {
        x = x - entity.hitbox.getPos().getX() - camera.pos.getX();
        y = y - entity.hitbox.getPos().getY() - camera.pos.getY();
        if (flip) {
            gc.save();
            // 위치는 이걸로 수정해야 함... 아마도?
            gc.translate((x + width) * camera.scale, y * camera.scale);
            gc.scale(-1, 1);
            gc.drawImage(img, 0, 0, width * camera.scale, height * camera.scale);
            gc.restore();
        } else {
            gc.drawImage(img, x * camera.scale, y * camera.scale, width * camera.scale, height * camera.scale);
        }
    }

    @Override
    public Sprite clone() {
      return new SimpleSprite(img, name);
    }
}
