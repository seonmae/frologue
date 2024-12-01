package com.banuh.frologue;

import com.banuh.frologue.core.tilemap.TileMapData;
import com.banuh.frologue.core.tilemap.TileMapLoader;
import com.banuh.frologue.game.FrologueGame;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
  private Map<String, PlayerInfo> players = new ConcurrentHashMap<>();
  private int playerX = 100, playerY = 100;
  private String playerState = "idle";  // 플레이어 상태 추가
  private String roomCode;
  private GraphicsContext gc;  // GraphicsContext 객체 클래스 멤버로 선언
  private Image idleImage, moveImage;   // 플레이어 이미지들

  private static class PlayerInfo {
    Point position;
    String state;
    PlayerInfo(Point position, String state) {
      this.position = position;
      this.state = state;
    }
  }

  @Override
  public void start(Stage stage) throws IOException {
    stage.setTitle("개구리다");

    Group root = new Group();
    Canvas canvas = new Canvas(300 * 3, 200 * 3);
    gc = canvas.getGraphicsContext2D();  // 클래스 멤버로 선언된 gc 할당
    root.getChildren().add(canvas);

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();

    gc.setImageSmoothing(false);

    FrologueGame game = new FrologueGame(canvas, 300, 200);
    game.run();

    // 캐릭터 이미지 로드
    idleImage = new Image("file:idle.png");
    moveImage = new Image("file:move.png");

    playerId = UUID.randomUUID().toString();
    players.put(playerId, new PlayerInfo(new Point(playerX, playerY), playerState));

    scene.setOnKeyPressed(event -> {
      boolean moved = false;
      switch (event.getCode()) {
        case LEFT:
          playerX -= 10;
          playerState = "move";
          moved = true;
          break;
        case RIGHT:
          playerX += 10;
          playerState = "move";
          moved = true;
          break;
        case UP:
          playerY -= 10;
          playerState = "move";
          moved = true;
          break;
        case DOWN:
          playerY += 10;
          playerState = "move";
          moved = true;
          break;
      }
      if (moved) {
        players.put(playerId, new PlayerInfo(new Point(playerX, playerY), playerState));
        sendPlayerPosition();
        drawPlayers(); // 키 입력 후 플레이어 위치를 다시 그림
      }
    });

    scene.setOnKeyReleased(event -> {
      playerState = "idle";
      players.put(playerId, new PlayerInfo(new Point(playerX, playerY), playerState));
      sendPlayerPosition();
      drawPlayers(); // 키 입력 후 플레이어 위치를 다시 그림
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
      String positionUpdate = "Position:" + playerId + "," + playerX + "," + playerY + "," + playerState;
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
          String state = data[3].trim();

          if (!pid.equals(playerId)) {
            players.put(pid, new PlayerInfo(new Point(x, y), state));
            System.out.println("Received update - Player ID: " + pid + ", X: " + x + ", Y: " + y + ", State: " + state);
          }
        }
        drawPlayers(); // 위치 정보를 수신한 후 플레이어들을 그림
      }
    } catch (Exception e) {
      System.out.println("Update receiving error: " + e.getMessage());
      reconnectToServer();
    }
  }

  private void drawPlayers() {
    gc.clearRect(0, 0, 900, 600); // 캔버스를 지움

    for (Map.Entry<String, PlayerInfo> entry : players.entrySet()) {
      PlayerInfo player = entry.getValue();
      Image imageToDraw = player.state.equals("move") ? moveImage : idleImage;
      gc.drawImage(imageToDraw, player.position.x, player.position.y, 20, 20); // 각 플레이어 상태에 맞는 이미지를 그림
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
