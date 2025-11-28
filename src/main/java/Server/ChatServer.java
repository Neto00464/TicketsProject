package Server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {
    private static final int PORT = 5000;
    private static Set<ClientHandler> clientHandlers = ConcurrentHashMap.newKeySet();
    private static Map<Integer, Set<ClientHandler>> tiqueteClients = new ConcurrentHashMap<>();
    private ServerSocket serverSocket;
    private boolean running = false;
    
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            running = true;
            System.out.println("Servidor de chat iniciado en puerto " + PORT);
            
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Nuevo cliente conectado: " + clientSocket.getInetAddress());
                    
                    ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                    clientHandlers.add(clientHandler);
                    
                    new Thread(clientHandler).start();
                } catch (SocketException e) {
                    if (!running) {
                        System.out.println("Servidor detenido");
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            
            for (ClientHandler handler : clientHandlers) {
                handler.closeConnection();
            }
            
            clientHandlers.clear();
            tiqueteClients.clear();
            
            System.out.println("Servidor detenido correctamente");
        } catch (IOException e) {
            System.err.println("Error al detener servidor: " + e.getMessage());
        }
    }
    
    public synchronized void addClientToTiquete(int tiqueteId, ClientHandler client) {
        tiqueteClients.computeIfAbsent(tiqueteId, k -> ConcurrentHashMap.newKeySet()).add(client);
        System.out.println("Cliente agregado al tiquete " + tiqueteId);
    }
    
    public synchronized void removeClientFromTiquete(int tiqueteId, ClientHandler client) {
        Set<ClientHandler> clients = tiqueteClients.get(tiqueteId);
        if (clients != null) {
            clients.remove(client);
            if (clients.isEmpty()) {
                tiqueteClients.remove(tiqueteId);
            }
        }
    }
    
    public synchronized void broadcastToTiquete(int tiqueteId, String message, ClientHandler sender) {
        Set<ClientHandler> clients = tiqueteClients.get(tiqueteId);
        if (clients != null) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }
    
    public void removeClient(ClientHandler handler) {
        clientHandlers.remove(handler);
        
        // Remover de todos los tiquetes
        for (Set<ClientHandler> clients : tiqueteClients.values()) {
            clients.remove(handler);
        }
    }
    
    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        
        // Agregar shutdown hook para cerrar el servidor limpiamente
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nCerrando servidor...");
            server.stop();
        }));
        
        server.start();
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private ChatServer server;
    private BufferedReader input;
    private PrintWriter output;
    private int userId;
    private int currentTiqueteId;
    
    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
        this.currentTiqueteId = -1;
    }
    
    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            
            String message;
            while ((message = input.readLine()) != null) {
                handleMessage(message);
            }
        } catch (IOException e) {
            System.out.println("Cliente desconectado: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }
    
    private void handleMessage(String message) {
        try {
            String[] parts = message.split("\\|", 3);
            String command = parts[0];
            
            switch (command) {
                case "JOIN":
                    if (parts.length >= 3) {
                        userId = Integer.parseInt(parts[1]);
                        currentTiqueteId = Integer.parseInt(parts[2]);
                        server.addClientToTiquete(currentTiqueteId, this);
                        sendMessage("JOINED|" + currentTiqueteId);
                    }
                    break;
                    
                case "MESSAGE":
                    if (parts.length >= 3 && currentTiqueteId != -1) {
                        String content = parts[2];
                        // Formato: MESSAGE|tiqueteId|userId:userName:content
                        server.broadcastToTiquete(currentTiqueteId, message, this);
                    }
                    break;
                    
                case "LEAVE":
                    if (currentTiqueteId != -1) {
                        server.removeClientFromTiquete(currentTiqueteId, this);
                        currentTiqueteId = -1;
                    }
                    break;
                    
                case "PING":
                    sendMessage("PONG");
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error procesando mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void sendMessage(String message) {
        if (output != null && !socket.isClosed()) {
            output.println(message);
        }
    }
    
    public void closeConnection() {
        try {
            if (currentTiqueteId != -1) {
                server.removeClientFromTiquete(currentTiqueteId, this);
            }
            server.removeClient(this);
            
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Error cerrando conexión: " + e.getMessage());
        }
    }
}
