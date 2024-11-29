package com.banuh.frologue;

import com.banuh.frologue.game.FrologueGame;
import com.banuh.frologue.game.scenes.TestScene;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class App extends Application {
  @Override
  public void start(Stage stage) {
    stage.setTitle("개구리다");

    Group root = new Group();
    Canvas canvas = new Canvas(800, 600);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    root.getChildren().add(canvas);

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();

    gc.setImageSmoothing(false);

    FrologueGame game = new FrologueGame(canvas, 800, 600);

    game.scale = 3;
    game.defaultURL = "file:src/main/resources/";
    game.run();
  }

  public static void main(String[] args) {
    launch(args);
  }
}