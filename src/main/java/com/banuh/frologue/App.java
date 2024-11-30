package com.banuh.frologue;

import com.banuh.frologue.core.tilemap.TileMapData;
import com.banuh.frologue.core.tilemap.TileMapLoader;
import com.banuh.frologue.game.FrologueGame;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    stage.setTitle("개구리다");

    Group root = new Group();
    Canvas canvas = new Canvas(300 * 3, 200 * 3);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    root.getChildren().add(canvas);

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();

    gc.setImageSmoothing(false);

    FrologueGame game = new FrologueGame(canvas, 300, 200);

    game.run();
  }

  public static void main(String[] args) {
    launch(args);
  }
}