package Client;

import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;
    
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private ChatMessageListener messageListener;
    private Thread listenerThread;
    private boolean connected = false;
    
    public interface ChatMessageListener {
        void onMessageReceived(String message);
        void onConnectionLost();
    }
    
    public boolean connect() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            connected = true;
            
            startListening();
            
            System.out.println("Conectado al servidor de chat");
            return true;
        } catch (IOException e) {
            System.err.println("Error conectando al servidor: " + e.getMessage());
            return false;
        }
    }
    
    private void startListening() {
        listenerThread = new Thread(() -> {
            try {
                String message;
                while (connected && (message = input.readLine()) != null) {
                    if (messageListener != null) {
                        final String msg = message;
                        javax.swing.SwingUtilities.invokeLater(() -> 
                            messageListener.onMessageReceived(msg)
                        );
                    }
                }
            } catch (IOException e) {
                if (connected) {
                    System.err.println("Conexión perdida: " + e.getMessage());
                    if (messageListener != null) {
                        javax.swing.SwingUtilities.invokeLater(() -> 
                            messageListener.onConnectionLost()
                        );
                    }
                }
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }
    
    public void joinTiquete(int userId, int tiqueteId) {
        if (connected && output != null) {
            output.println("JOIN|" + userId + "|" + tiqueteId);
        }
    }
    
    public void leaveTiquete() {
        if (connected && output != null) {
            output.println("LEAVE");
        }
    }
    
    public void sendMessage(int tiqueteId, String userName, String content) {
        if (connected && output != null) {
            output.println("MESSAGE|" + tiqueteId + "|" + userName + ":" + content);
        }
    }
    
    public void disconnect() {
        connected = false;
        try {
            if (output != null) output.close();
            if (input != null) input.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("Desconectado del servidor");
        } catch (IOException e) {
            System.err.println("Error al desconectar: " + e.getMessage());
        }
    }
    
    public void setMessageListener(ChatMessageListener listener) {
        this.messageListener = listener;
    }
    
    public boolean isConnected() {
        return connected && socket != null && !socket.isClosed();
    }
}