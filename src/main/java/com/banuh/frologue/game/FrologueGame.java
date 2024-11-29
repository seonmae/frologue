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
        addSprite("frog_death", "img/Frog/Death.png", 1, 4, 8);
        addSprite("frog_fall", "img/Frog/Fall.png");
        addSprite("frog_hurt", "img/Frog/Hurt.png", 1, 4, 8);
        addSprite("frog_idle", "img/Frog/Idle.png", 2, 2, 8);
        addSprite("frog_jump", "img/Frog/Jump.png");
        addSprite("frog_land", "img/Frog/Land.png", 1, 2, 8);
        addSprite("frog_move", "img/Frog/Land.png", 1, 2, 8);
        addSprite("frog_turn", "img/Frog/Turn.png");
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
