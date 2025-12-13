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

public class TecnicoGUI extends JFrame {
    private Usuario usuario;
    private TiqueteDAO tiqueteDAO;
    private ChatClient chatClient;
    
    private JTable tablaTiquetes;
    private DefaultTableModel modeloTabla;
    private JButton btnVerDetalles;
    private JButton btnActualizar;
    private JButton btnCerrarSesion;
    private JTabbedPane tabbedPane;
    
    public TecnicoGUI(Usuario usuario) {
        this.usuario = usuario;
        this.tiqueteDAO = new TiqueteDAO();
        this.chatClient = new ChatClient();
        initUI();
        cargarTiquetesAsignados();
        
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
        setTitle("Sistema de Tiquetes - Técnico IT: " + usuario.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(210, 210, 210));
        
        // Panel superior con título
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(16, 59, 66));
        
        JLabel titleLabel = new JLabel("Gestión de Tiquetes IT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Pestañas
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        // Tabla de tiquetes asignados
        JPanel asignadosPanel = crearPanelTiquetes();
        tabbedPane.addTab("Mis Tiquetes Asignados", asignadosPanel);
        
        // Tabla de todos los tiquetes
        JPanel todosPanel = crearPanelTodosTiquetes();
        tabbedPane.addTab("Todos los Tiquetes", todosPanel);
        
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex == 0) {
                cargarTiquetesAsignados();
            } else {
                cargarTodosTiquetes();
            }
        });
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(new Color(210, 210, 210));
        
        btnVerDetalles = crearBoton("Ver Detalles / Chat", new Color(16, 59, 66));
        btnActualizar = crearBoton("Actualizar", new Color(16, 59, 66));
        btnCerrarSesion = crearBoton("Cerrar Sesion", new Color(180, 50, 50));
        
        buttonPanel.add(btnVerDetalles);
        buttonPanel.add(btnActualizar);
        buttonPanel.add(btnCerrarSesion);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        
        // Eventos
        btnVerDetalles.addActionListener(e -> verDetalles());
        btnActualizar.addActionListener(e -> {
            if (tabbedPane.getSelectedIndex() == 0) {
                cargarTiquetesAsignados();
            } else {
                cargarTodosTiquetes();
            }
        });
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }
    
    private JPanel crearPanelTiquetes() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columnas = {"ID Tiquete", "Titulo", "Estado", "Fecha Creacion", "Tipo"};
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
        panel.add(scrollPane, BorderLayout.CENTER);
        
        tablaTiquetes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    verDetalles();
                }
            }
        });
        
        return panel;
    }
    
    private DefaultTableModel modeloTablaTodos;
    private JTable tablaTodos;
    
    private JPanel crearPanelTodosTiquetes() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columnas = {"ID", "Titulo", "Estado", "Creador", "Asignado", "Prioridad"};
        modeloTablaTodos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaTodos = new JTable(modeloTablaTodos);
        tablaTodos.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaTodos.setRowHeight(30);
        tablaTodos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tablaTodos.getTableHeader().setBackground(Color.BLACK);
        tablaTodos.getTableHeader().setForeground(Color.WHITE);
        tablaTodos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tablaTodos);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        tablaTodos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    verDetallesTodos();
                }
            }
        });
        
        return panel;
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
    
    private void cargarTiquetesAsignados() {
        modeloTabla.setRowCount(0);
        
        try {
            List<Tiquete> tiquetes = tiqueteDAO.obtenerPorAsignado(usuario.getId());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            
            for (Tiquete t : tiquetes) {
                String fecha = t.getFechaCreacion() != null ? 
                               t.getFechaCreacion().format(formatter) : "";
                
                modeloTabla.addRow(new Object[]{
                    "IT" + t.getId(),
                    t.getTitulo(),
                    formatearEstado(t.getEstado()),
                    fecha,
                    t.getCategoria()
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
    
    private void cargarTodosTiquetes() {
        modeloTablaTodos.setRowCount(0);
        
        try {
            List<Tiquete> tiquetes = tiqueteDAO.obtenerTodos();
            
            for (Tiquete t : tiquetes) {
                String asignado = t.getAsignadoNombre() != null ? 
                                 t.getAsignadoNombre() : "Sin asignar";
                
                modeloTablaTodos.addRow(new Object[]{
                    "IT" + t.getId(),
                    t.getTitulo(),
                    formatearEstado(t.getEstado()),
                    t.getCreadorNombre(),
                    asignado,
                    t.getPrioridad().toUpperCase()
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
        
        mostrarDetalle(tiqueteId);
    }
    
    private void verDetallesTodos() {
        int selectedRow = tablaTodos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione un tiquete",
                "Selección requerida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String idStr = (String) modeloTablaTodos.getValueAt(selectedRow, 0);
        int tiqueteId = Integer.parseInt(idStr.replace("IT", ""));
        
        mostrarDetalle(tiqueteId);
    }
    
    private void mostrarDetalle(int tiqueteId) {
        try {
            Tiquete tiquete = tiqueteDAO.buscarPorId(tiqueteId);
            if (tiquete != null) {
                new DetalleTiqueteDialog(this, tiquete, usuario, chatClient, true).setVisible(true);
                // Actualizar después de cerrar el diálogo
                if (tabbedPane.getSelectedIndex() == 0) {
                    cargarTiquetesAsignados();
                } else {
                    cargarTodosTiquetes();
                }
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