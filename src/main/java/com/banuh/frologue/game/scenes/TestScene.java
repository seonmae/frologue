package com.banuh.frologue.game.scenes;

import com.banuh.frologue.core.Game;
import com.banuh.frologue.core.entity.Entity;
import com.banuh.frologue.core.scene.GameScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TestScene extends GameScene {
    public double GRAVITY = 9.8;
    public double JUMP_STRENGTH = 500;
    public double SPEED = 100;
    public int FLAT = 100;
    public Entity frog;

    public TestScene(Game game, String name) {
        super(game, name);
    }

    @Override
    public void start() {
        frog = game.addEntity("frog_idle", 0, 0);
        frog.addStates(new String[]{
            "frog_move",
            "frog_jump",
            "frog_fall",
            "frog_land",
            "frog_hurt",
            "frog_death",
        }).addVelocity("move").addVelocity("gravity");

        addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            KeyCode key = event.getCode();

            if (key == KeyCode.SPACE) {
                frog.setVelocityY("move", -JUMP_STRENGTH);
            }
        });
    }

    @Override
    public void update() {
        if (frog.pos.getY() < FLAT) {
            frog.getVelocity("gravity").addedY(GRAVITY);
        } else {
            if (frog.getState("frog_fall")) {
                frog.setState("frog_land", true);
                game.setTimeout(() -> {
                    frog.setState("frog_land", false);
                }, game.FRAME() * 4);
            }
            frog.pos.setY(FLAT);
            frog.setVelocityY("move", 0);
            frog.setVelocityY("gravity", 0);
        }

        double totalVelocityY = frog.getTotalVelocity().getY();

        if (game.isPressed.leftKey) {
            frog.isFlip = false;
            frog.setVelocityX("move", -SPEED);
            frog.setState("frog_move", true);
        } else if (game.isPressed.rightKey) {
            frog.isFlip = true;
            frog.setVelocityX("move", SPEED);
            frog.setState("frog_move", true);
        } else {
            frog.setVelocityX("move", 0);
            frog.setState("frog_move", false);
        }

        frog.setState("frog_jump", totalVelocityY < 0);
        frog.setState("frog_fall", totalVelocityY > 0);
    }
}
