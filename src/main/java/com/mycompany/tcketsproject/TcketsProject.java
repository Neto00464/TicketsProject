package com.mycompany.tcketsproject;

import Logica.UsuarioManager;
import UI.LoginGUI;
import javax.swing.SwingUtilities;

public class TcketsProject {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            UsuarioManager usuarioManager = new UsuarioManager();

            LoginGUI login = new LoginGUI(usuarioManager);

            login.setVisible(true);
        });
    }
}