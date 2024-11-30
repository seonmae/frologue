package com.banuh.frologue.game.scenes;

import com.banuh.frologue.core.Game;
import com.banuh.frologue.core.scene.GameScene;
import com.banuh.frologue.core.tilemap.OverLap;
import com.banuh.frologue.core.tilemap.TileMap;
import com.banuh.frologue.core.utils.Direction;
import com.banuh.frologue.core.utils.Vector2D;
import com.banuh.frologue.game.entity.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.Objects;

public class TestScene extends GameScene {
    public double GRAVITY = 9.8;
    public final int TILE_SIZE = 16;
    public Frog frog;
    OverLap overLap;

    public TestScene(Game game, String name) {
        super(game, name);
    }

    @Override
    public void update() {
        frog.setVelocityX("wall", 0);
        OverLap overLap = isCollision(frog.getNextPos(), frog.getWidth(), frog.getHeight());

        this.overLap = overLap;

//        if (overLap.is) {
//            System.out.println("TOP:    " + overLap.isTop);
//            System.out.println("RIGHT:  " + overLap.isRight);
//            System.out.println("BOTTOM: " + overLap.isBottom);
//            System.out.println("LEFT:   " + overLap.isLeft);
//            System.out.println();
//        }

        if (overLap.isRight || overLap.isLeft) {
            if (frog.getState("jump")) { // 점프하다가 벽에 닿은 경우
                frog.getVelocity("move_with_jump").multiplied(-1);
                frog.isFlip = !frog.isFlip;
            } else { // 그냥 벽에 닿은 경우
                if (overLap.isLeft) {
                    frog.pos.setX(overLap.leftTilePos.getX() + TILE_SIZE);
                } else if (overLap.isRight) {
                    frog.pos.setX(overLap.leftTilePos.getX() - frog.getWidth());
                }
            }
        }

        if (overLap.isTop) {
            // 점프하다가 천장에 닿은 경우
            if (frog.getState("jump")) {
                frog.getVelocity("move").flipY();
                frog.getVelocity("move").multiplied(0.25);
            }
        }

        // 바닥에 닿아있는 경우
        if (overLap.isBottom) {
            frog.setOnGround(true);

            if (frog.getState("fall")) {
                frog.setState("jump", false);
                frog.getVelocity("move_with_jump").reset();

                frog.setState("land");
                game.setTimeout(() -> {
                    frog.setState("land", false);
                }, game.FRAME() * 4);
            }

            frog.pos.setY(overLap.bottomTilePos.getY() - frog.getHeight());
            frog.setVelocityY("move", 0);
            frog.setVelocityY("gravity", 0);
        } else {
            frog.setOnGround(false);
            frog.getVelocity("gravity").addedY(GRAVITY);
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
        if (overLap.isTop) {
            game.gc.setFill(new Color(1f, 0f, 0f, 0.5f));
            game.gc.fillRect(overLap.topTilePos.getX() * game.camera.scale, overLap.topTilePos.getY() * game.camera.scale, 48, 48);
        }
        if (overLap.isRight) {
            game.gc.setFill(new Color(0f, 1f, 0f, 0.5f));
            game.gc.fillRect(overLap.rightTilePos.getX() * game.camera.scale, overLap.rightTilePos.getY() * game.camera.scale, 48, 48);
        }
        if (overLap.isBottom) {
            game.gc.setFill(new Color(0f, 0f, 1f, 0.5f));
            game.gc.fillRect(overLap.bottomTilePos.getX() * game.camera.scale, overLap.bottomTilePos.getY() * game.camera.scale, 48, 48);
        }
        if (overLap.isLeft) {
            game.gc.setFill(new Color(0f, 0f, 0f, 0.5f));
            game.gc.fillRect(overLap.leftTilePos.getX() * game.camera.scale, overLap.leftTilePos.getY() * game.camera.scale, 48, 48);
        }

//        if (tilepos != null) {
//            // show hitboxes
//            game.gc.setStroke(Color.RED);
//            game.gc.strokeRect(tilepos.getX() * game.camera.scale, tilepos.getY() * game.camera.scale, tilewidth * game.camera.scale, tileheight * game.camera.scale);
//            game.gc.strokeRect(frog.pos.getX() * game.camera.scale, (frog.pos.getY()) * game.camera.scale, frog.getWidth() * game.camera.scale, frog.getHeight() * game.camera.scale);
//
//        }

//        if (overLap.is) {
//            game.gc.setFill(new Color(1f, 0f, 0f, 0.5f));
//            game.gc.fillRect(
//                    overLap.from.getX() * game.camera.scale,
//                    overLap.from.getY() * game.camera.scale,
//                    overLap.to.getX() * game.camera.scale,
//                    overLap.to.getY() * game.camera.scale
//            );
//        }
    }

    @Override
    public void start() {
        game.showHitbox = true;

        frog = (Frog)game.addEntity(new WitchFrog(180, 0, game));
        TileMap tileMap = game.tileMapList.get("test");
        game.placeTileMapByBottom("test", - (tileMap.getWidth() - game.width) / 2f, 200 + 32);

        addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            KeyCode key = event.getCode();

            if (key == KeyCode.SPACE && !frog.getState("charging") && !frog.getState("jump")) {
                frog.jump_scale = 1;
                frog.setState("charging");
            }
        });

        addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            KeyCode key = event.getCode();

            if (key == KeyCode.SPACE && !frog.getState("jump")) {
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
}
