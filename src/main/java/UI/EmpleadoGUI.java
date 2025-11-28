package UI;

import Client.ChatClient;
import DAO.TiqueteDAO;
import Model.Tiquete;
import Model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmpleadoGUI extends JFrame {
    private Usuario usuario;
    private TiqueteDAO tiqueteDAO;
    private ChatClient chatClient;
    
    private JTable tablaTiquetes;
    private DefaultTableModel modeloTabla;
    private JButton btnNuevoTiquete;
    private JButton btnActualizar;
    private JButton btnVerDetalles;
    private JButton btnCerrarSesion;
    
    public EmpleadoGUI(Usuario usuario) {
        this.usuario = usuario;
        this.tiqueteDAO = new TiqueteDAO();
        this.chatClient = new ChatClient();
        initUI();
        cargarTiquetes();
        
        // Conectar al servidor de chat
        if (!chatClient.connect()) {
            JOptionPane.showMessageDialog(this,
                "No se pudo conectar al servidor de chat",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void initUI() {
        try {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    setTitle("Sistema de Tiquetes - Empleado: " + usuario.getNombre());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sistema de Tiquetes - Empleado: " + usuario.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(210, 210, 210));
        
        // Panel superior con título
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(16, 59, 66));
        
        JLabel titleLabel = new JLabel("Mis Tiquetes", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Tabla de tiquetes
        String[] columnas = {"ID Tiquete", "Titulo", "Estado", "Fecha Creacion", "Soporte IT"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaTiquetes = new JTable(modeloTabla);
        tablaTiquetes.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaTiquetes.setRowHeight(30);
        tablaTiquetes.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tablaTiquetes.getTableHeader().setBackground(Color.BLACK);
        tablaTiquetes.getTableHeader().setForeground(Color.WHITE);
        tablaTiquetes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tablaTiquetes);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(new Color(210, 210, 210));
        
        btnNuevoTiquete = crearBoton("Nuevo Tiquete", new Color(16, 59, 66));
        btnVerDetalles = crearBoton("Ver Detalles / Chat", new Color(16, 59, 66));
        btnActualizar = crearBoton("Actualizar", new Color(16, 59, 66));
        btnCerrarSesion = crearBoton("Cerrar Sesion", new Color(180, 50, 50));
        
        buttonPanel.add(btnNuevoTiquete);
        buttonPanel.add(btnVerDetalles);
        buttonPanel.add(btnActualizar);
        buttonPanel.add(btnCerrarSesion);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        
        // Eventos
        btnNuevoTiquete.addActionListener(e -> abrirNuevoTiquete());
        btnVerDetalles.addActionListener(e -> verDetalles());
        btnActualizar.addActionListener(e -> cargarTiquetes());
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        
        tablaTiquetes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    verDetalles();
                }
            }
        });
    }
    
    private JButton crearBoton(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);              
        btn.setBorderPainted(true);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg.darker(), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        return btn;
    }
    
    private void cargarTiquetes() {
        modeloTabla.setRowCount(0);
        
        try {
            List<Tiquete> tiquetes = tiqueteDAO.obtenerPorCreador(usuario.getId());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            
            for (Tiquete t : tiquetes) {
                String fecha = t.getFechaCreacion() != null ? 
                               t.getFechaCreacion().format(formatter) : "";
                String soporte = t.getAsignadoNombre() != null ? 
                                t.getAsignadoNombre() : "Sin asignar";
                
                modeloTabla.addRow(new Object[]{
                    "IT" + t.getId(),
                    t.getTitulo(),
                    formatearEstado(t.getEstado()),
                    fecha,
                    soporte
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar tiquetes: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private String formatearEstado(String estado) {
        switch (estado) {
            case "abierto": return "En Progreso";
            case "en_progreso": return "En Progreso";
            case "resuelto": return "Completado";
            case "cerrado": return "Completado";
            default: return estado;
        }
    }
    
    private void abrirNuevoTiquete() {
        new NuevoTiqueteDialog(this, usuario).setVisible(true);
        cargarTiquetes();
    }
    
    private void verDetalles() {
        int selectedRow = tablaTiquetes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione un tiquete",
                "Selección requerida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String idStr = (String) modeloTabla.getValueAt(selectedRow, 0);
        int tiqueteId = Integer.parseInt(idStr.replace("IT", ""));
        
        try {
            Tiquete tiquete = tiqueteDAO.buscarPorId(tiqueteId);
            if (tiquete != null) {
                new DetalleTiqueteDialog(this, tiquete, usuario, chatClient, false).setVisible(true);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar tiquete: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea cerrar sesión?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            chatClient.disconnect();
            new LoginGUI().setVisible(true);
            dispose();
        }
    }
}
