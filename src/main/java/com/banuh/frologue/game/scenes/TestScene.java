package com.banuh.frologue.game.scenes;

import com.banuh.frologue.core.Game;
import com.banuh.frologue.core.entity.Entity;
import com.banuh.frologue.core.scene.GameScene;
import com.banuh.frologue.game.entity.Frog;
import com.banuh.frologue.game.entity.NormalFrog;
import com.banuh.frologue.game.entity.SpaceFrog;
import com.banuh.frologue.game.entity.UmbrellaFrog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class TestScene extends GameScene {
    public double GRAVITY = 9.8;
    public int FLAT = 100;
    public Frog frog;

    public TestScene(Game game, String name) {
        super(game, name);
    }

    @Override
    public void start() {
        frog = (Frog)game.addEntity(new NormalFrog(150, 0, game));

        addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            KeyCode key = event.getCode();

            if (key == KeyCode.SPACE && !frog.getState("charging")) {
                frog.jump_scale = 1;
                frog.setState("charging");
            }
        });

        addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            KeyCode key = event.getCode();

            if (key == KeyCode.SPACE) {
                frog.setVelocityY("move", -frog.JUMP_STRENGTH * frog.jump_scale);
                frog.setVelocityX("move_with_jump", 100 * (frog.isFlip ? 1 : -1));
                /*frog.SPEED *= 3;*/
                frog.setState("charging", false);
                frog.setState("charged", false);
                frog.setState("jump");
                frog.jump_scale = 1;
            }
        });
    }

    @Override
    public void update() {
        frog.setVelocityX("wall", 0);

        // 점프하다가 벽에 닿은 경우
        if (frog.getState("jump")) {
            if (frog.getY() <= 0) {
                frog.getVelocity("move").setY(-frog.getVelocity("move").getY());
            }

            if (frog.getX() <= 0 || frog.getX() + frog.getWidth() >= game.width) {
                frog.getVelocity("move_with_jump").multiplied(-1);
                frog.isFlip = !frog.isFlip;
            }
        }

        if (frog.getX() <= 0 || frog.getX() + frog.getWidth() >= game.width) {
            frog.setVelocityX("wall", -frog.getVelocity("move").getX());
        }

        if (frog.getY() < FLAT) {
            frog.getVelocity("gravity").addedY(GRAVITY);
        } else {
            // 바닥에 닿은 경우
            if (frog.getState("fall")) {
                frog.setState("jump", false);
                frog.getVelocity("move_with_jump").reset();

                frog.setState("land");
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
            frog.setVelocityX("move", -frog.SPEED);
            frog.setState("move", true);
        } else if (game.isPressed.rightKey) {
            if (!frog.isFlip) {
                frog.setState("turn", true);
                frog.isFlip = true;
                game.setTimeout(() -> {
                    frog.setState("turn", false);
                }, game.FRAME() * 2);
            }
            frog.setVelocityX("move", frog.SPEED);
            frog.setState("move", true);
        } else {
            frog.setVelocityX("move", 0);
            frog.setState("move", false);
        }

        // 차징 중이나 점프 중엔 키보드로 이동못함!
        if (frog.getState("charging") || frog.getState("jump")) {
            frog.setVelocityX("move", 0);
            frog.setState("move", false);
        }

        if (game.isPressed.spaceKey) {
            if (frog.jump_scale < 2.5) {
                frog.jump_scale += 1.25f / game.getFps();
            } else {
                frog.jump_scale = 2.5;
            }
        }

        if (frog.jump_scale >= 2.5) {
            frog.setState("charged");
        }

        frog.setState("up", totalVelocityY < 0);
        frog.setState("fall", totalVelocityY > 0);
    }

    @Override
    public void render() {
        System.out.println(frog.jump_scale);
    }
}
