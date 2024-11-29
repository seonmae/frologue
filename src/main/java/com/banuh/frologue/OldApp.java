package com.banuh.frologue;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class OldApp extends Application {

    private static final int COLS = 2;
    private static final int ROWS = 2;
    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;
    private static final int TOTAL_FRAMES = COLS * ROWS;

    private static final double SCALE = 3;
    private int frameIndex = 0;
    private long lastFrameTime = 0;
    private static final long FRAME_DURATION = 100_000_000;

    private double frogX = 400;
    private double frogY = 300;
    private double velocityY = 0;
    private boolean isJumping = false;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean turnLeft = false;
    private boolean turnRight = false;
    private boolean isFacingRight = false;
    private boolean isFalling = false;
    private boolean isLanding = false;
    private long landingStartTime = 0;
    private int landingFrameIndex = 0;
    private static final double GRAVITY = 0.5;
    private static final double JUMP_STRENGTH = -15;
    private static final double MOVE_SPEED = 3;

    private Image spriteSheet;
    private Image jumpImage;
    private Image fallImage;
    private Image movementImage;
    private Image landingImage;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("개구리 점프 게임");

        Group root = new Group();
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        spriteSheet = new Image("file:src/main/resources/img/Frog/Idle.png");
        jumpImage = new Image("file:src/main/resources/img/Frog/Jump.png");
        fallImage = new Image("file:src/main/resources/img/Frog/Fall.png");
        movementImage = new Image("file:src/main/resources/img/Frog/Land.png");
        landingImage = new Image("file:src/main/resources/img/Frog/Aland.png");
        scene.setOnKeyPressed(event -> {
            KeyCode key = event.getCode();
            switch (key) {
                case A:
                    if (!movingLeft) {
                        turnLeft = true;
                        isFacingRight = false;
                    }
                    movingLeft = true;
                    break;
                case D:
                    if (!movingRight) {
                        turnRight = true;
                        isFacingRight = true;
                    }
                    movingRight = true;
                    break;
                case UP:
                case SPACE:
                    if (!isJumping && !isFalling) {
                        velocityY = JUMP_STRENGTH;
                        isJumping = true;
                    }
                    break;
            }
        });

        scene.setOnKeyReleased(event -> {
            KeyCode key = event.getCode();
            switch (key) {
                case A:
                    movingLeft = false;
                    turnLeft = false;
                    break;
                case D:
                    movingRight = false;
                    turnRight = false;
                    break;
            }
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastFrameTime >= FRAME_DURATION && !isJumping && !isFalling && !isLanding && !movingLeft && !movingRight) {
                    frameIndex = (frameIndex + 1) % TOTAL_FRAMES;
                    lastFrameTime = now;
                }

                if (movingLeft || movingRight) {
                    frogX += movingRight ? MOVE_SPEED : -MOVE_SPEED;
                    if (now - lastFrameTime >= FRAME_DURATION) {
                        landingFrameIndex = (landingFrameIndex + 1) % 2;
                        lastFrameTime = now;
                    }
                }

                if (isJumping) {
                    frogY += velocityY;
                    velocityY += GRAVITY;

                    if (velocityY > 0) {
                        isJumping = false;
                        isFalling = true;
                    }
                }

                if (isFalling) {
                    frogY += velocityY;
                    velocityY += GRAVITY;

                    if (frogY >= 300) {
                        frogY = 300;
                        isFalling = false;
                        isLanding = true;
                        landingStartTime = now;
                        velocityY = 0;
                    }
                }

                if (isLanding && now - landingStartTime > FRAME_DURATION) {
                    isLanding = false;
                }

                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                Image currentImage;
                int srcX = 0;
                int srcY = 0;
                if (isJumping) {
                    currentImage = jumpImage;
                } else if (isFalling) {
                    currentImage = fallImage;
                } else if (movingLeft || movingRight) {
                    currentImage = movementImage;
                    srcX = landingFrameIndex * WIDTH;
                    srcY = 0;
                } else if (isLanding) {
                    currentImage = landingImage;
                } else {
                    currentImage = spriteSheet;
                    if (!isJumping && !isFalling && !isLanding) {
                        int rowIndex = frameIndex / COLS;
                        int colIndex = frameIndex % COLS;
                        srcX = colIndex * WIDTH;
                        srcY = rowIndex * HEIGHT;
                    }
                }

                gc.save();
                if (isFacingRight) {
                    gc.translate(frogX + WIDTH * SCALE, frogY);
                    gc.scale(-1, 1);
                    gc.drawImage(currentImage, srcX, srcY, WIDTH, HEIGHT, 0, 0, WIDTH * SCALE, HEIGHT * SCALE);
                } else {
                    gc.drawImage(currentImage, srcX, srcY, WIDTH, HEIGHT, frogX, frogY, WIDTH * SCALE, HEIGHT * SCALE);
                }
                gc.restore();
            }
        }.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
