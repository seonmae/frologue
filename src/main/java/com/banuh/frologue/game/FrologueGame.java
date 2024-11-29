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
        String[] types = {"normal", "ox", "space", "umbrella", "witch"};
        for (String type: types) {
            addSprite("frog-"+type+"-death", "img/frog-"+type+"/death.png", 1, 4, 8);
            addSprite("frog-"+type+"-fall", "img/frog-"+type+"/fall.png");
            addSprite("frog-"+type+"-hurt", "img/frog-"+type+"/hurt.png", 1, 4, 8);
            addSprite("frog-"+type+"-idle", "img/frog-"+type+"/idle.png", 2, 2, 8);
            addSprite("frog-"+type+"-jump", "img/frog-"+type+"/jump.png");
            addSprite("frog-"+type+"-land", "img/frog-"+type+"/land.png", 1, 2, 8);
            addSprite("frog-"+type+"-move", "img/frog-"+type+"/land.png", 1, 2, 8);
            addSprite("frog-"+type+"-turn", "img/frog-"+type+"/turn.png");
        }
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
        this.showHitbox = true;
    }
}
