package com.banuh.frologue;

import java.net.Socket;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.UUID;

public class AppTest {
    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;
    private boolean connected = false;
    private String playerName;

    public AppTest() {
        connectToServer();
        // 게임 초기화 로직 및 서버 통신 로직 추가
    }

    private void connectToServer() {
        try {
            String serverIP = "127.0.0.1";
            int serverPort = 1235;
            socket = new Socket(serverIP, serverPort);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

            // 서버에 연결 메시지 전송
            writer.write("MakeConnection");
            writer.newLine();
            writer.flush();
            writer.write("plsSend");
            writer.newLine();
            writer.flush();
            playerName = "Player_" + UUID.randomUUID();
            writer.write(playerName);
            writer.newLine();
            writer.flush();
            connected = true;

            // 서버로부터 수신하는 스레드 시작
            new Thread(this::receiveUpdates).start();
        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }

    private void sendPlayerPosition(int x, int y) {
        try {
            if (connected && writer != null) {
                String positionUpdate = "Position:" + x + "," + y;
                writer.write(positionUpdate);
                writer.newLine();
                writer.flush();
            }
        } catch (Exception e) {
            System.out.println("location data sending error: " + e.getMessage());
            connected = false;
        }
    }

    private void receiveUpdates() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                if (message.startsWith("Position:")) {
                    String[] parts = message.split(",");
                    int playerX = Integer.parseInt(parts[0].split(":")[1].trim());
                    int playerY = Integer.parseInt(parts[1].trim());
                    // 받은 위치 정보를 바탕으로 게임 화면 갱신
                    updatePlayerPosition(playerX, playerY);
                }
            }
        } catch (Exception e) {
            System.out.println("location data recv error: " + e.getMessage());
            connected = false;
        }
    }

    private void updatePlayerPosition(int x, int y) {
        // 화면에 플레이어 위치를 업데이트하는 로직 구현
    }

    public static void main(String[] args) {
        new App();
    }
}
