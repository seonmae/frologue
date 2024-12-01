package com.banuh.frologue.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

class User {
    String name;
    InetAddress ip;
    int x, y;

    User(String _name, InetAddress _ip) {
        name = _name;
        ip = _ip;
    }
}

class Room {
    String roomCode;
    List<Clients> clients;

    Room(String roomCode) {
        this.roomCode = roomCode;
        this.clients = new CopyOnWriteArrayList<>();
    }

    void addClient(Clients client) {
        clients.add(client);
    }

    void broadcast(String message, Clients senderClient) {
        for (Clients client : clients) {
            if (client != senderClient) {
                client.sendMessage(message);
            }
        }
    }
}

class Clients implements Runnable {
    Socket sock;
    BufferedWriter tcpWriter;
    BufferedReader tcpReader;
    InetAddress connectedIP = null;
    Room room;

    Clients(Socket _sock) {
        sock = _sock;
        connectedIP = sock.getInetAddress();
    }

    @Override
    public void run() {
        try {
            tcpReader = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-8"));
            tcpWriter = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"));

            String request = tcpReader.readLine();

            if (request.equals("CreateRoom")) {
                String roomCode = ServerBase.createRoom(this);
                tcpWriter.write("Room Created: " + roomCode);
                tcpWriter.newLine();
                tcpWriter.flush();
                System.out.println("Room Created with code: " + roomCode);
                room = ServerBase.getRoom(roomCode);
            } else if (request.startsWith("JoinRoom")) {
                String roomCode = request.split(" ")[1];
                room = ServerBase.getRoom(roomCode);
                if (room != null) {
                    room.addClient(this);
                    tcpWriter.write("Joined Room: " + roomCode);
                    tcpWriter.newLine();
                    tcpWriter.flush();
                    System.out.println("Client joined room: " + roomCode);
                } else {
                    tcpWriter.write("Invalid Room Code");
                    tcpWriter.newLine();
                    tcpWriter.flush();
                }
            } else {
                tcpWriter.write("Invalid Request");
                tcpWriter.newLine();
                tcpWriter.flush();
                sock.close();
                return;
            }

            String message;
            while ((message = tcpReader.readLine()) != null) {
                if (room != null && message.startsWith("Position:")) {
                    // 위치와 상태 정보를 포함한 메시지를 브로드캐스트
                    room.broadcast(message, this);
                }
            }
        } catch (Exception e) {
            System.out.println("Client connection error: " + e.getMessage());
        } finally {
            try {
                if (room != null) {
                    room.clients.remove(this);
                }
                if (sock != null && !sock.isClosed()) {
                    sock.close();
                }
            } catch (Exception e) {
                System.out.println("Error closing client connection: " + e.getMessage());
            }
        }
    }

    public void sendMessage(String message) {
        try {
            tcpWriter.write(message);
            tcpWriter.newLine();
            tcpWriter.flush();
        } catch (Exception e) {
            System.out.println("Message sending failed: " + e.getMessage());
        }
    }
}

public class ServerBase {
    private static Map<String, Room> rooms = new HashMap<>();

    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("setOutError - 184");
            e.printStackTrace();
        }

        try {
            System.out.println("Server trying to start...");
            ServerSocket tcpSock = new ServerSocket(1235);
            ExecutorService executorService = Executors.newCachedThreadPool();

            while (true) {
                Socket tcpClientSock = tcpSock.accept();
                System.out.println("Client Connected From " + tcpClientSock.getRemoteSocketAddress());

                Clients newClient = new Clients(tcpClientSock);
                executorService.execute(newClient);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String generateRoomCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    public static String createRoom(Clients client) {
        String roomCode = generateRoomCode();
        Room room = new Room(roomCode);
        room.addClient(client);
        rooms.put(roomCode, room);
        return roomCode;
    }

    public static Room getRoom(String roomCode) {
        return rooms.get(roomCode);
    }
}
