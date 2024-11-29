package com.banuh.frologue.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

public class GameClientTest extends JPanel {
    private Map<String, Point> players = new ConcurrentHashMap<>();
    private String playerId;
    private int playerX = 100, playerY = 100;
    private Socket socket;
    private BufferedWriter writer;
    private String roomCode;

    public GameClientTest() {
        setFocusable(true);
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        playerId = UUID.randomUUID().toString();
        players.put(playerId, new Point(playerX, playerY));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        playerX -= 10;
                        break;
                    case KeyEvent.VK_RIGHT:
                        playerX += 10;
                        break;
                    case KeyEvent.VK_UP:
                        playerY -= 10;
                        break;
                    case KeyEvent.VK_DOWN:
                        playerY += 10;
                        break;
                }
                players.put(playerId, new Point(playerX, playerY));
                sendPlayerPosition();
                repaint();
            }
        });

        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket("127.0.0.1", 1235);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

            String roomInput = JOptionPane.showInputDialog(this,
                    "Enter room code to join, or leave blank to create new room:");

            if (roomInput != null && !roomInput.trim().isEmpty()) {
                writer.write("JoinRoom " + roomInput.trim());
                writer.newLine();
                writer.flush();
                String response = reader.readLine();
                if (response.startsWith("Joined Room")) {
                    roomCode = response.split(": ")[1];
                    JOptionPane.showMessageDialog(this, "Joined room: " + roomCode);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to join room: " + response);
                    System.exit(1);
                }
            } else {
                writer.write("CreateRoom");
                writer.newLine();
                writer.flush();
                String response = reader.readLine();
                roomCode = response.split(": ")[1];
                JOptionPane.showMessageDialog(this, "Created room: " + roomCode);
            }

            new Thread(this::receiveUpdates).start();
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
            JOptionPane.showMessageDialog(this, "Failed to reconnect to server");
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
                        repaint();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Update receiving error: " + e.getMessage());
            reconnectToServer();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Map.Entry<String, Point> player : players.entrySet()) {
            Point pos = player.getValue();
            if (player.getKey().equals(playerId)) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.BLUE);
            }
            g.fillRect(pos.x, pos.y, 50, 50);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Simple Multiplayer Game");
            GameClientTest gamePanel = new GameClientTest();
            frame.add(gamePanel);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}