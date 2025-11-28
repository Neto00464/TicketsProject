package UI;

import DAO.UsuarioDAO;
import Model.Usuario;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.SQLException;

public class LoginGUI extends JFrame {
    private UsuarioDAO usuarioDAO;
    private JTextField correoField;
    private JPasswordField passField;
    private JButton btnLogin;
    private JButton btnRegister;
    
    public LoginGUI() {
        this.usuarioDAO = new UsuarioDAO();
        initUI();
    }
    
    private void initUI() {
        setTitle("Sistema de Tiqueteria - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        
        Color bg = new Color(210, 210, 210);
        Color borderColor = new Color(16, 59, 66);
        LineBorder thickBorder = new LineBorder(borderColor, 4, true);
        
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(bg);
        content.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setContentPane(content);
        
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(12, 12, 12, 12);
        c.fill = GridBagConstraints.HORIZONTAL;
        
        // TÍTULO
        JLabel title = new JLabel("Sistema de Tiqueteria", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setBorder(thickBorder);
        title.setOpaque(true);
        title.setBackground(bg);
        title.setPreferredSize(new Dimension(320, 60));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        content.add(title, c);
        
        // CORREO
        c.gridwidth = 1;
        JLabel correoLabel = new JLabel("Correo Electronico:");
        correoLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        c.gridx = 0; c.gridy = 1; c.weightx = 0.3;
        content.add(correoLabel, c);
        
        correoField = new JTextField();
        correoField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        correoField.setBorder(thickBorder);
        c.gridx = 1; c.gridy = 1; c.weightx = 0.7;
        content.add(correoField, c);
        
        // CONTRASEÑA
        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        c.gridx = 0; c.gridy = 2;
        content.add(passLabel, c);
        
        passField = new JPasswordField();
        passField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        passField.setBorder(thickBorder);
        c.gridx = 1; c.gridy = 2;
        content.add(passField, c);
        
        // BOTÓN LOGIN
        btnLogin = new JButton("Iniciar Sesion");
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnLogin.setBorder(thickBorder);
        btnLogin.setBackground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        c.gridx = 0; c.gridy = 3;
        content.add(btnLogin, c);
        
        btnLogin.addActionListener(e -> handleLogin());
        
        // BOTÓN REGISTRAR
        btnRegister = new JButton("Crear Cuenta");
        btnRegister.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnRegister.setBorder(thickBorder);
        btnRegister.setBackground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        c.gridx = 1; c.gridy = 3;
        content.add(btnRegister, c);
        
        btnRegister.addActionListener(e -> abrirRegistro());
        
        // Enter key en password field
        passField.addActionListener(e -> handleLogin());
    }
    
    private void handleLogin() {
        String correo = correoField.getText().trim();
        String password = new String(passField.getPassword());
        
        if (correo.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor complete todos los campos",
                "Campos vacíos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Usuario usuario = usuarioDAO.autenticar(correo, password);
            
            if (usuario != null) {
                JOptionPane.showMessageDialog(this,
                    "Bienvenido " + usuario.getNombre(),
                    "Login exitoso",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Abrir la ventana correspondiente según el rol
                if (usuario.getRol().equals("empleado")) {
                    new EmpleadoGUI(usuario).setVisible(true);
                } else if (usuario.getRol().equals("tecnico") || usuario.getRol().equals("admin")) {
                    new TecnicoGUI(usuario).setVisible(true);
                }
                
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Correo o contraseña incorrectos",
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE);
                passField.setText("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error de conexión: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void abrirRegistro() {
        new RegistroGUI(this).setVisible(true);
        setVisible(false);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginGUI().setVisible(true);
        });
    }
}