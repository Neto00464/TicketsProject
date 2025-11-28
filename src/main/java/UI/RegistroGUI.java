/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI;

import DAO.UsuarioDAO;
import Model.Usuario;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RegistroGUI extends JFrame {
    private UsuarioDAO usuarioDAO;
    private LoginGUI loginGUI;
    
    private JTextField nombreField;
    private JTextField correoField;
    private JPasswordField passField;
    private JPasswordField confirmPassField;
    private JTextField fechaField;
    private JTextField cedulaField;
    private JComboBox<String> rolCombo;
    private JButton btnRegistrar;
    private JButton btnVolver;
    
    public RegistroGUI(LoginGUI loginGUI) {
        this.usuarioDAO = new UsuarioDAO();
        this.loginGUI = loginGUI;
        initUI();
    }
    
    private void initUI() {
        setTitle("Sistema de Tiqueteria - Registro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 650);
        setLocationRelativeTo(null);
        
        Color bg = new Color(210, 210, 210);
        Color borderColor = new Color(16, 59, 66);
        LineBorder thickBorder = new LineBorder(borderColor, 4, true);
        
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(bg);
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(content);
        
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        
        // TÍTULO
        JLabel title = new JLabel("Crear Cuenta Nueva", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setBorder(thickBorder);
        title.setOpaque(true);
        title.setBackground(bg);
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        content.add(title, c);
        
        c.gridwidth = 1;
        
        // NOMBRE COMPLETO
        addField(content, c, "Nombre Completo:", nombreField = new JTextField(), 1, thickBorder);
        
        // CORREO
        addField(content, c, "Correo Electronico:", correoField = new JTextField(), 2, thickBorder);
        
        // CONTRASEÑA
        addField(content, c, "Contraseña:", passField = new JPasswordField(), 3, thickBorder);
        
        // CONFIRMAR CONTRASEÑA
        addField(content, c, "Confirmar Contraseña:", confirmPassField = new JPasswordField(), 4, thickBorder);
        
        // FECHA DE NACIMIENTO
        JLabel fechaLabel = new JLabel("Fecha Nacimiento (dd/MM/yyyy):");
        fechaLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        c.gridx = 0; c.gridy = 5; c.weightx = 0.4;
        content.add(fechaLabel, c);
        
        fechaField = new JTextField();
        fechaField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        fechaField.setBorder(thickBorder);
        c.gridx = 1; c.gridy = 5; c.weightx = 0.6;
        content.add(fechaField, c);
        
        // CÉDULA
        addField(content, c, "Cédula:", cedulaField = new JTextField(), 6, thickBorder);
        
        // ROL
        JLabel rolLabel = new JLabel("Tipo de Usuario:");
        rolLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        c.gridx = 0; c.gridy = 7;
        content.add(rolLabel, c);
        
        rolCombo = new JComboBox<>(new String[]{"empleado", "tecnico"});
        rolCombo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        rolCombo.setBorder(thickBorder);
        c.gridx = 1; c.gridy = 7;
        content.add(rolCombo, c);
        
        // BOTÓN REGISTRAR
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnRegistrar.setBorder(thickBorder);
        btnRegistrar.setBackground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        c.gridx = 0; c.gridy = 8;
        content.add(btnRegistrar, c);
        
        btnRegistrar.addActionListener(e -> handleRegistro());
        
        // BOTÓN VOLVER
        btnVolver = new JButton("Volver al Login");
        btnVolver.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnVolver.setBorder(thickBorder);
        btnVolver.setBackground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        c.gridx = 1; c.gridy = 8;
        content.add(btnVolver, c);
        
        btnVolver.addActionListener(e -> {
            loginGUI.setVisible(true);
            dispose();
        });
    }
    
    private void addField(JPanel panel, GridBagConstraints c, String labelText, 
                         JTextField field, int row, LineBorder border) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        c.gridx = 0; c.gridy = row; c.weightx = 0.4;
        panel.add(label, c);
        
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBorder(border);
        c.gridx = 1; c.gridy = row; c.weightx = 0.6;
        panel.add(field, c);
    }
    
    private void handleRegistro() {
        String nombre = nombreField.getText().trim();
        String correo = correoField.getText().trim();
        String password = new String(passField.getPassword());
        String confirmPass = new String(confirmPassField.getPassword());
        String fechaStr = fechaField.getText().trim();
        String cedula = cedulaField.getText().trim();
        String rol = (String) rolCombo.getSelectedItem();
        
        // Validaciones
        if (nombre.isEmpty() || correo.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor complete todos los campos obligatorios",
                "Campos vacíos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this,
                "Las contraseñas no coinciden",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this,
                "La contraseña debe tener al menos 6 caracteres",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        LocalDate fechaNacimiento = null;
        if (!fechaStr.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                fechaNacimiento = LocalDate.parse(fechaStr, formatter);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Use dd/MM/yyyy",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        try {
            // Verificar si el correo ya existe
            if (usuarioDAO.buscarPorCorreo(correo) != null) {
                JOptionPane.showMessageDialog(this,
                    "Este correo ya está registrado",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setCorreo(correo);
            nuevoUsuario.setPassword(password);
            nuevoUsuario.setRol(rol);
            nuevoUsuario.setFechaNacimiento(fechaNacimiento);
            nuevoUsuario.setCedula(cedula);
            
            boolean registrado = usuarioDAO.registrar(nuevoUsuario);
            
            if (registrado) {
                JOptionPane.showMessageDialog(this,
                    "Registro exitoso. Ahora puede iniciar sesión",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                
                loginGUI.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al registrar usuario",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error de base de datos: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}