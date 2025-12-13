/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistematiquetes;


import Server.ChatServer;
import UI.LoginGUI;
import javax.swing.*;

public class SistemaTiquetesMain {
    
    public static void main(String[] args) {
        // Configurar el look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Preguntar si se desea iniciar el servidor
        int opcion = JOptionPane.showConfirmDialog(null,
            "¿Desea iniciar el servidor de chat?\n\n" +
            "Seleccione:\n" +
            "- SÍ: Para iniciar el servidor (solo una vez)\n" +
            "- NO: Para conectarse como cliente",
            "Configuración del Sistema",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (opcion == JOptionPane.YES_OPTION) {
            // Iniciar servidor en un hilo separado
            new Thread(() -> {
                ChatServer server = new ChatServer();
                System.out.println("Iniciando servidor de chat...");
                server.start();
            }).start();
            
            JOptionPane.showMessageDialog(null,
                "Servidor iniciado correctamente.\n" +
                "Ahora puede abrir otras ventanas como clientes.",
                "Servidor Activo",
                JOptionPane.INFORMATION_MESSAGE);
        }
        
        // Iniciar la interfaz de login
        SwingUtilities.invokeLater(() -> {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setVisible(true);
        });
    }
}