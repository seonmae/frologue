package com.banuh.frologue.core.sprite;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.time.Instant;

public class AnimationSprite extends Sprite {
    private int currentFrame = 0;
    private int frameLength;
    private double deltaTime;
    private long lastChange;

    public final int row;
    public final int col;

    public AnimationSprite(String src, int row, int col, int frameLength, double deltaTime, String name) {
        super(src, name);

        this.row = row;
        this.col = col;

        this.frameLength = frameLength;

        this.height = (int)img.getHeight() / row;
        this.width = (int)img.getWidth() / col;

        this.deltaTime = deltaTime;
    }

    public AnimationSprite(Image img, int row, int col, int frameLength, double deltaTime, String name) {
        super(img, name);

        this.row = row;
        this.col = col;

        this.frameLength = frameLength;

        this.height = (int)img.getHeight() / row;
        this.width = (int)img.getWidth() / col;

        this.deltaTime = deltaTime;
    }

    public void draw(GraphicsContext gc, double x, double y, double scale, boolean flip) {
        long now = Instant.now().toEpochMilli(); // 밀리초 단위
        long gap = now - lastChange;

        if (lastChange == 0) {
            lastChange = now;
            return;
        }

        if (gap > deltaTime) {
            currentFrame += (int)(gap / deltaTime);
            currentFrame = (frameLength <= currentFrame) ? (currentFrame % frameLength) : currentFrame;

            this.lastChange = now;
        }

        int sx = width * (currentFrame % col);
        int sy = height * (int)(currentFrame / col);

        if (flip) {
            gc.save();
            gc.translate(x + width * scale, y);
            gc.scale(-1, 1);
            gc.drawImage(
                img, sx, sy, width, height,
                0, 0,
                width * scale, height * scale
            );
            gc.restore();
        } else {
            gc.drawImage(
                img, sx, sy, width, height,
                x, y,
                width * scale, height * scale
            );
        }
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double scale) {
        draw(gc, x, y, scale, false);
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    @Override
    public AnimationSprite clone() {
        AnimationSprite sprite = new AnimationSprite(img, row, col, frameLength, deltaTime, name);
        return sprite;
    }
}