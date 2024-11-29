package com.banuh.frologue.game;

import com.banuh.frologue.core.Game;
import com.banuh.frologue.core.entity.Entity;
import com.banuh.frologue.core.utils.Vector2D;
import com.banuh.frologue.game.scenes.TestScene;
import javafx.scene.canvas.Canvas;

public class FrologueGame extends Game {
    public FrologueGame(Canvas canvas, int width, int height) {
        super(canvas, width, height);
    }

    @Override
    public void preload() {
        addSprite("frog-normal-death", "img/normal-frog/frog-normal-death.png", 1, 4, 8);
        addSprite("frog-normal-fall", "img/normal-frog/frog-normal-fall.png");
        addSprite("frog-normal-hurt", "img/normal-frog/frog-normal-hurt.png", 1, 4, 8);
        addSprite("frog-normal-idle", "img/normal-frog/frog-normal-idle.png", 2, 2, 8);
        addSprite("frog-normal-jump", "img/normal-frog/frog-normal-jump.png");
        addSprite("frog-normal-land", "img/normal-frog/frog-normal-land.png", 1, 2, 8);
        addSprite("frog-normal-move", "img/normal-frog/frog-normal-land.png", 1, 2, 8);
        addSprite("frog-normal-turn", "img/normal-frog/frog-normal-turn.png");
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {

    }

    @Override
    public void start() {
        this.addScene(new TestScene(this, "test"));
        this.setCurrentScene("test");
    }
}
