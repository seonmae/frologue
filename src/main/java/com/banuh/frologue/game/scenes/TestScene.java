package com.banuh.frologue.game.scenes;

import com.banuh.frologue.core.Game;
import com.banuh.frologue.core.entity.Entity;
import com.banuh.frologue.core.scene.GameScene;
import com.banuh.frologue.game.entity.Frog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TestScene extends GameScene {
    public double GRAVITY = 9.8 * 2;
    public double JUMP_STRENGTH = 600;
    public double SPEED = 100;
    public int FLAT = 300;
    public Entity frog;

    public TestScene(Game game, String name) {
        super(game, name);
    }

    @Override
    public void start() {
        frog = game.addEntity(new Frog("normal", 0, 0, game));

        addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            KeyCode key = event.getCode();

            if (key == KeyCode.SPACE) {
                frog.setVelocityY("move", -JUMP_STRENGTH);
            }
        });
    }

    @Override
    public void update() {
        if (frog.getY() < FLAT) {
            frog.getVelocity("gravity").addedY(GRAVITY);
        } else {
            if (frog.getState("fall")) {
                frog.setState("land", true);
                game.setTimeout(() -> {
                    frog.setState("land", false);
                }, game.FRAME() * 4);
            }
            frog.setY(FLAT);
            frog.setVelocityY("move", 0);
            frog.setVelocityY("gravity", 0);
        }

        double totalVelocityY = frog.getTotalVelocity().getY();

        if (game.isPressed.leftKey) {
            if (frog.isFlip) {
                frog.setState("turn", true);
                frog.isFlip = false;
                game.setTimeout(() -> {
                    frog.setState("turn", false);
                }, game.FRAME() * 2);
            }
            frog.setVelocityX("move", -SPEED);
            frog.setState("move", true);
        } else if (game.isPressed.rightKey) {
            if (!frog.isFlip) {
                frog.setState("turn", true);
                frog.isFlip = true;
                game.setTimeout(() -> {
                    frog.setState("turn", false);
                }, game.FRAME() * 2);
            }
            frog.setVelocityX("move", SPEED);
            frog.setState("move", true);
        } else {
            frog.setVelocityX("move", 0);
            frog.setState("move", false);
        }

        frog.setState("jump", totalVelocityY < 0);
        frog.setState("fall", totalVelocityY > 0);
    }
}
