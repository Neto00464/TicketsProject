package UI;

import com.mycompany.tcketsproject.Usuario;
import Logica.UsuarioManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LoginGUI extends JFrame {

    private UsuarioManager usuarioManager;

    private JTextField correoField;
    private JPasswordField passField;
    private JTextField fechaField;
    private JTextField nombreField;
    private JTextField cedulaField;

    private JButton btnLogin;
    private JButton btnRegister;

    public LoginGUI(UsuarioManager usuarioManager) {
        this.usuarioManager = usuarioManager;
        initUI();
    }

    private void initUI() {
        setTitle("Sistema de Tiqueteria");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        // Paleta visual
        Color bg = new Color(210, 210, 210);         // gris claro
        Color borderColor = new Color(16, 59, 66);   // azul/teal oscuro
        LineBorder thickBorder = new LineBorder(borderColor, 4, true);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(bg);
        content.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setContentPane(content);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(12, 12, 12, 12);
        c.fill = GridBagConstraints.HORIZONTAL;

        // ===========================
        // TÍTULO
        // ===========================
        JLabel title = new JLabel("Sistema de Tiqueteria", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.PLAIN, 32));
        title.setBorder(thickBorder);
        title.setOpaque(true);
        title.setBackground(bg);
        title.setPreferredSize(new Dimension(320, 70));
        c.gridx = 0; 
        c.gridy = 0; 
        c.gridwidth = 2;
        content.add(title, c);

        // ===========================
        // CORREO
        // ===========================
        JLabel correoLabel = new JLabel("Correo Electronico:");
        correoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        c.gridx = 0; c.gridy = 1; c.gridwidth = 1; c.weightx = 0.3;
        content.add(correoLabel, c);

        correoField = new JTextField();
        correoField.setFont(new Font("SansSerif", Font.PLAIN, 20));
        correoField.setBorder(thickBorder);
        c.gridx = 1; c.gridy = 1; c.weightx = 0.7;
        content.add(correoField, c);

        // ===========================
        // CONTRASEÑA
        // ===========================
        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        c.gridx = 0; c.gridy = 2;
        content.add(passLabel, c);

        passField = new JPasswordField();
        passField.setFont(new Font("SansSerif", Font.PLAIN, 20));
        passField.setBorder(thickBorder);
        c.gridx = 1; c.gridy = 2;
        content.add(passField, c);

        // ===========================
        // FECHA DE NACIMIENTO
        // ===========================
        JLabel fechaLabel = new JLabel("Fecha de Nacimiento:");
        fechaLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        c.gridx = 0; c.gridy = 3;
        content.add(fechaLabel, c);

        fechaField = new JTextField();
        fechaField.setFont(new Font("SansSerif", Font.PLAIN, 20));
        fechaField.setBorder(thickBorder);
        c.gridx = 1; c.gridy = 3;
        content.add(fechaField, c);

        // ===========================
        // BOTÓN LOGIN
        // ===========================
        btnLogin = new JButton("Iniciar Sesion");
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 20));
        btnLogin.setBorder(thickBorder);
        btnLogin.setBackground(Color.WHITE);
        btnLogin.setFocusPainted(false);

        c.gridx = 0; 
        c.gridy = 5; 
        c.gridwidth = 1;
        content.add(btnLogin, c);

        btnLogin.addActionListener(e -> {
            String correo = correoField.getText();
            String pass = String.valueOf(passField.getPassword());

            boolean ok = usuarioManager.validarLogin(correo, pass);

            if (ok) {
                JOptionPane.showMessageDialog(this,
                    "Inicio de sesión correcto",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(this,
                    "Correo o contraseña incorrectos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ===========================
        // BOTÓN REGISTRARSE
        // ===========================
        btnRegister = new JButton("Registrarse");
        btnRegister.setFont(new Font("SansSerif", Font.BOLD, 20));
        btnRegister.setBorder(thickBorder);
        btnRegister.setBackground(Color.WHITE);
        btnRegister.setFocusPainted(false);

        c.gridx = 1; 
        c.gridy = 5;
        content.add(btnRegister, c);

        btnRegister.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Aquí abrirás la pantalla de registro",
                "Registro",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
    }
}
