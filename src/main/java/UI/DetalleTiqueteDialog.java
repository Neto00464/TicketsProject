package UI;

import Client.ChatClient;
import DAO.TiqueteDAO;
import DAO.UsuarioDAO;
import Model.Tiquete;
import Model.Usuario;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DetalleTiqueteDialog extends JDialog implements ChatClient.ChatMessageListener {
    private Tiquete tiquete;
    private Usuario usuario;
    private ChatClient chatClient;
    private boolean esTecnico;
    private TiqueteDAO tiqueteDAO;
    private UsuarioDAO usuarioDAO;
    
    // Componentes de UI
    private JTextArea chatArea;
    private JTextField mensajeField;
    private JButton btnEnviar;
    private JComboBox<String> estadoCombo;
    private JComboBox<Usuario> tecnicoCombo;
    private JButton btnGuardarCambios;
    private JButton btnCerrar;
    
    private JLabel lblEstado;
    private JLabel lblAsignado;
    
    public DetalleTiqueteDialog(JFrame parent, Tiquete tiquete, Usuario usuario, 
                                ChatClient chatClient, boolean esTecnico) {
        super(parent, "Detalles: IT" + tiquete.getId() + " - " + tiquete.getTitulo(), true);
        this.tiquete = tiquete;
        this.usuario = usuario;
        this.chatClient = chatClient;
        this.esTecnico = esTecnico;
        this.tiqueteDAO = new TiqueteDAO();
        this.usuarioDAO = new UsuarioDAO();
        
        initUI();
        setupChat();
        cargarInformacion();
    }
    
    private void initUI() {
        setSize(1000, 700);
        setLocationRelativeTo(getParent());
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);
        
        // Panel superior con información del tiquete
        JPanel infoPanel = crearPanelInformacion();
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        
        // Panel central con chat
        JPanel chatPanel = crearPanelChat();
        mainPanel.add(chatPanel, BorderLayout.CENTER);
        
        // Panel inferior con botones
        JPanel buttonPanel = crearPanelBotones();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel crearPanelInformacion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(16, 59, 66), 2),
            "Información del Tiquete",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 16),
            new Color(16, 59, 66)
        ));
        
        // Panel izquierdo con detalles
        JPanel leftPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        leftPanel.add(crearLabelInfo("ID:", "IT" + tiquete.getId()));
        leftPanel.add(crearLabelInfo("Título:", tiquete.getTitulo()));
        leftPanel.add(crearLabelInfo("Prioridad:", tiquete.getPrioridad().toUpperCase()));
        leftPanel.add(crearLabelInfo("Categoría:", tiquete.getCategoria()));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fecha = tiquete.getFechaCreacion() != null ? 
                      tiquete.getFechaCreacion().format(formatter) : "N/A";
        leftPanel.add(crearLabelInfo("Fecha:", fecha));
        leftPanel.add(crearLabelInfo("Creador:", tiquete.getCreadorNombre()));
        
        panel.add(leftPanel, BorderLayout.WEST);
        
        // Panel derecho con descripción y controles
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel descLabel = new JLabel("Descripción:");
        descLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        rightPanel.add(descLabel, BorderLayout.NORTH);
        
        JTextArea descArea = new JTextArea(tiquete.getDescripcion());
        descArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBackground(new Color(245, 245, 245));
        JScrollPane scrollDesc = new JScrollPane(descArea);
        scrollDesc.setPreferredSize(new Dimension(300, 80));
        rightPanel.add(scrollDesc, BorderLayout.CENTER);
        
        // Controles para técnicos
        if (esTecnico) {
            JPanel controlPanel = new JPanel(new GridLayout(2, 2, 10, 5));
            controlPanel.setBackground(Color.WHITE);
            
            JLabel estadoLabel = new JLabel("Estado:");
            estadoLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            controlPanel.add(estadoLabel);
            
            estadoCombo = new JComboBox<>(new String[]{
                "abierto", "en_progreso", "resuelto", "cerrado"
            });
            estadoCombo.setSelectedItem(tiquete.getEstado());
            controlPanel.add(estadoCombo);
            
            JLabel tecnicoLabel = new JLabel("Asignar a:");
            tecnicoLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            controlPanel.add(tecnicoLabel);
            
            tecnicoCombo = new JComboBox<>();
            try {
                List<Usuario> tecnicos = usuarioDAO.obtenerTecnicos();
                for (Usuario tec : tecnicos) {
                    tecnicoCombo.addItem(tec);
                }
                
                if (tiquete.getAsignadoId() != null) {
                    Usuario asignado = usuarioDAO.buscarPorId(tiquete.getAsignadoId());
                    if (asignado != null) {
                        tecnicoCombo.setSelectedItem(asignado);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            controlPanel.add(tecnicoCombo);
            
            rightPanel.add(controlPanel, BorderLayout.SOUTH);
        } else {
            // Para empleados, solo mostrar info
            JPanel statusPanel = new JPanel(new GridLayout(2, 1, 5, 5));
            statusPanel.setBackground(Color.WHITE);
            
            lblEstado = new JLabel("Estado: " + formatearEstado(tiquete.getEstado()));
            lblEstado.setFont(new Font("SansSerif", Font.BOLD, 13));
            statusPanel.add(lblEstado);
            
            String asignado = tiquete.getAsignadoNombre() != null ? 
                             tiquete.getAsignadoNombre() : "Sin asignar";
            lblAsignado = new JLabel("Soporte IT: " + asignado);
            lblAsignado.setFont(new Font("SansSerif", Font.PLAIN, 13));
            statusPanel.add(lblAsignado);
            
            rightPanel.add(statusPanel, BorderLayout.SOUTH);
        }
        
        panel.add(rightPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearLabelInfo(String label, String value) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(Color.WHITE);
        
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        panel.add(lblLabel);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(lblValue);
        
        return panel;
    }
    
    private JPanel crearPanelChat() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(16, 59, 66), 2),
            "Chat en Tiempo Real",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 16),
            new Color(16, 59, 66)
        ));
        
        // Área de chat
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollChat = new JScrollPane(chatArea);
        scrollChat.setPreferredSize(new Dimension(0, 300));
        panel.add(scrollChat, BorderLayout.CENTER);
        
        // Panel de envío de mensajes
        JPanel sendPanel = new JPanel(new BorderLayout(10, 0));
        sendPanel.setBackground(Color.WHITE);
        sendPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        mensajeField = new JTextField();
        mensajeField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        mensajeField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(16, 59, 66), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        sendPanel.add(mensajeField, BorderLayout.CENTER);
        
        btnEnviar = new JButton("Enviar");
        btnEnviar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnEnviar.setBackground(new Color(16, 59, 66));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFocusPainted(false);
        btnEnviar.setPreferredSize(new Dimension(100, 40));
        btnEnviar.addActionListener(e -> enviarMensaje());
        sendPanel.add(btnEnviar, BorderLayout.EAST);
        
        panel.add(sendPanel, BorderLayout.SOUTH);
        
        mensajeField.addActionListener(e -> enviarMensaje());
        
        return panel;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(Color.WHITE);
        
        if (esTecnico) {
            btnGuardarCambios = new JButton("Guardar Cambios");
            btnGuardarCambios.setFont(new Font("SansSerif", Font.BOLD, 14));
            btnGuardarCambios.setBackground(new Color(16, 59, 66));
            btnGuardarCambios.setForeground(Color.WHITE);
            btnGuardarCambios.setFocusPainted(false);
            btnGuardarCambios.addActionListener(e -> guardarCambios());
            panel.add(btnGuardarCambios);
        }
        
        btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCerrar.setBackground(new Color(150, 150, 150));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(e -> cerrar());
        panel.add(btnCerrar);
        
        return panel;
    }
    
    private void setupChat() {
        if (chatClient.isConnected()) {
            chatClient.setMessageListener(this);
            chatClient.joinTiquete(usuario.getId(), tiquete.getId());
        } else {
            JOptionPane.showMessageDialog(this,
                "El chat no está disponible. No hay conexión con el servidor.",
                "Chat no disponible",
                JOptionPane.WARNING_MESSAGE);
            mensajeField.setEnabled(false);
            btnEnviar.setEnabled(false);
        }
    }
    
    private void cargarInformacion() {
        chatArea.append("=== Bienvenido al chat del tiquete IT" + tiquete.getId() + " ===\n");
        chatArea.append("Usuario: " + usuario.getNombre() + "\n");
        chatArea.append("========================================\n\n");
    }
    
    private void enviarMensaje() {
        String mensaje = mensajeField.getText().trim();
        
        if (mensaje.isEmpty()) {
            return;
        }
        
        if (!chatClient.isConnected()) {
            JOptionPane.showMessageDialog(this,
                "No hay conexión con el servidor de chat",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        chatClient.sendMessage(tiquete.getId(), usuario.getNombre(), mensaje);
        
        // Mostrar mensaje propio
        agregarMensaje(usuario.getNombre(), mensaje);
        
        mensajeField.setText("");
    }
    
    private void agregarMensaje(String autor, String contenido) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String hora = java.time.LocalTime.now().format(formatter);
        
        chatArea.append("[" + hora + "] " + autor + ": " + contenido + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
    
    @Override
    public void onMessageReceived(String message) {
        try {
            String[] parts = message.split("\\|", 3);
            if (parts.length >= 3 && parts[0].equals("MESSAGE")) {
                String[] contentParts = parts[2].split(":", 2);
                if (contentParts.length >= 2) {
                    String autor = contentParts[0];
                    String contenido = contentParts[1];
                    agregarMensaje(autor, contenido);
                }
            }
        } catch (Exception e) {
            System.err.println("Error procesando mensaje: " + e.getMessage());
        }
    }
    
    @Override
    public void onConnectionLost() {
        SwingUtilities.invokeLater(() -> {
            chatArea.append("\n*** Conexión con el servidor perdida ***\n");
            mensajeField.setEnabled(false);
            btnEnviar.setEnabled(false);
        });
    }
    
    private void guardarCambios() {
        String nuevoEstado = (String) estadoCombo.getSelectedItem();
        Usuario tecnicoSeleccionado = (Usuario) tecnicoCombo.getSelectedItem();
        
        try {
            boolean cambios = false;
            
            // Actualizar estado
            if (!nuevoEstado.equals(tiquete.getEstado())) {
                tiqueteDAO.cambiarEstado(tiquete.getId(), nuevoEstado);
                cambios = true;
            }
            
            // Actualizar asignación
            if (tecnicoSeleccionado != null) {
                Integer nuevoAsignadoId = tecnicoSeleccionado.getId();
                if (!nuevoAsignadoId.equals(tiquete.getAsignadoId())) {
                    tiqueteDAO.asignarTecnico(tiquete.getId(), nuevoAsignadoId);
                    cambios = true;
                }
            }
            
            if (cambios) {
                JOptionPane.showMessageDialog(this,
                    "Cambios guardados exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Recargar tiquete
                tiquete = tiqueteDAO.buscarPorId(tiquete.getId());
            } else {
                JOptionPane.showMessageDialog(this,
                    "No hay cambios para guardar",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar cambios: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void cerrar() {
        if (chatClient.isConnected()) {
            chatClient.leaveTiquete();
        }
        dispose();
    }
    
    private String formatearEstado(String estado) {
        switch (estado) {
            case "abierto": return "Abierto";
            case "en_progreso": return "En Progreso";
            case "resuelto": return "Resuelto";
            case "cerrado": return "Cerrado";
            default: return estado;
        }
    }
}