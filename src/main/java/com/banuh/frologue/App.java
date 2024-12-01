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

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

public class App extends Application {
  private Socket socket;
  private BufferedWriter writer;
  private String playerId;
  private Map<String, Point> players = new ConcurrentHashMap<>();
  private int playerX = 100, playerY = 100;
  private String roomCode;

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

    playerId = UUID.randomUUID().toString();
    players.put(playerId, new Point(playerX, playerY));

    scene.setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case LEFT:
          playerX -= 10;
          break;
        case RIGHT:
          playerX += 10;
          break;
        case UP:
          playerY -= 10;
          break;
        case DOWN:
          playerY += 10;
          break;
      }
      players.put(playerId, new Point(playerX, playerY));
      sendPlayerPosition();
    });

    connectToServer();
    new Thread(this::receiveUpdates).start();
  }

  private void connectToServer() {
    try {
      socket = new Socket("127.0.0.1", 1235);
      writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

      String roomInput = JOptionPane.showInputDialog(null,
              "Enter room code to join, or leave blank to create new room:");

      if (roomInput != null && !roomInput.trim().isEmpty()) {
        writer.write("JoinRoom " + roomInput.trim());
        writer.newLine();
        writer.flush();
        String response = reader.readLine();
        if (response.startsWith("Joined Room")) {
          roomCode = response.split(": ")[1];
          JOptionPane.showMessageDialog(null, "Joined room: " + roomCode);
        } else {
          JOptionPane.showMessageDialog(null, "Failed to join room: " + response);
          System.exit(1);
        }
      } else {
        writer.write("CreateRoom");
        writer.newLine();
        writer.flush();
        String response = reader.readLine();
        roomCode = response.split(": ")[1];
        JOptionPane.showMessageDialog(null, "Created room: " + roomCode);
      }
    } catch (Exception e) {
      e.printStackTrace();
      reconnectToServer();
    }
  }

  private void reconnectToServer() {
    try {
      System.out.println("Lost connection, reconnecting...");
      if (socket != null && !socket.isClosed()) {
        socket.close();
      }
      connectToServer();
    } catch (Exception e) {
      System.out.println("Reconnection failed: " + e.getMessage());
      JOptionPane.showMessageDialog(null, "Failed to reconnect to server");
      System.exit(1);
    }
  }

  private void sendPlayerPosition() {
    try {
      String positionUpdate = "Position:" + playerId + "," + playerX + "," + playerY;
      writer.write(positionUpdate);
      writer.newLine();
      writer.flush();
    } catch (Exception e) {
      System.out.println("Position sending error: " + e.getMessage());
      reconnectToServer();
    }
  }

  private void receiveUpdates() {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"))) {
      String message;
      while ((message = reader.readLine()) != null) {
        if (message.startsWith("Position:")) {
          String[] parts = message.split(":");
          String[] data = parts[1].split(",");
          String pid = data[0].trim();
          int x = Integer.parseInt(data[1].trim());
          int y = Integer.parseInt(data[2].trim());

          if (!pid.equals(playerId)) {
            players.put(pid, new Point(x, y));
          }
        }
      }
    } catch (Exception e) {
      System.out.println("Update receiving error: " + e.getMessage());
      reconnectToServer();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
