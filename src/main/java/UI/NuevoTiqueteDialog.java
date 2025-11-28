package UI;

import DAO.TiqueteDAO;
import Model.Tiquete;
import Model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class NuevoTiqueteDialog extends JDialog {
    private TiqueteDAO tiqueteDAO;
    private Usuario usuario;
    
    private JTextField tituloField;
    private JTextArea descripcionArea;
    private JComboBox<String> prioridadCombo;
    private JComboBox<String> categoriaCombo;
    private JButton btnCrear;
    private JButton btnCancelar;
    
    public NuevoTiqueteDialog(JFrame parent, Usuario usuario) {
        super(parent, "Crear Nuevo Tiquete", true);
        this.usuario = usuario;
        this.tiqueteDAO = new TiqueteDAO();
        initUI();
    }
    
    private void initUI() {
        setSize(600, 550);
        setLocationRelativeTo(getParent());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(210, 210, 210));
        
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        
        // Título
        JLabel titleLabel = new JLabel("Crear Nuevo Tiquete de Soporte", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        mainPanel.add(titleLabel, c);
        
        c.gridwidth = 1;
        
        // Título del tiquete
        addLabel(mainPanel, c, "Título:", 1);
        tituloField = new JTextField(30);
        tituloField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        c.gridx = 1; c.gridy = 1;
        mainPanel.add(tituloField, c);
        
        // Descripción
        addLabel(mainPanel, c, "Descripción:", 2);
        descripcionArea = new JTextArea(6, 30);
        descripcionArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descripcionArea.setLineWrap(true);
        descripcionArea.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(descripcionArea);
        c.gridx = 1; c.gridy = 2;
        c.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollDesc, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        
        // Prioridad
        addLabel(mainPanel, c, "Prioridad:", 3);
        prioridadCombo = new JComboBox<>(new String[]{"baja", "media", "alta", "urgente"});
        prioridadCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        c.gridx = 1; c.gridy = 3;
        mainPanel.add(prioridadCombo, c);
        
        // Categoría
        addLabel(mainPanel, c, "Categoría:", 4);
        categoriaCombo = new JComboBox<>(new String[]{
            "Hardware", "Software", "Red", "Email", "Impresora", "Otro"
        });
        categoriaCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        c.gridx = 1; c.gridy = 4;
        mainPanel.add(categoriaCombo, c);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(210, 210, 210));
        
        btnCrear = new JButton("Crear Tiquete");
        btnCrear.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCrear.setBackground(new Color(16, 59, 66));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);
        btnCrear.addActionListener(e -> crearTiquete());
        
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCancelar.setBackground(new Color(150, 150, 150));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());
        
        buttonPanel.add(btnCrear);
        buttonPanel.add(btnCancelar);
        
        c.gridx = 0; c.gridy = 5; c.gridwidth = 2;
        mainPanel.add(buttonPanel, c);
        
        setContentPane(mainPanel);
    }
    
    private void addLabel(JPanel panel, GridBagConstraints c, String text, int row) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        c.gridx = 0; c.gridy = row;
        panel.add(label, c);
    }
    
    private void crearTiquete() {
        String titulo = tituloField.getText().trim();
        String descripcion = descripcionArea.getText().trim();
        String prioridad = (String) prioridadCombo.getSelectedItem();
        String categoria = (String) categoriaCombo.getSelectedItem();
        
        if (titulo.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor complete todos los campos",
                "Campos vacíos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Tiquete nuevoTiquete = new Tiquete();
        nuevoTiquete.setTitulo(titulo);
        nuevoTiquete.setDescripcion(descripcion);
        nuevoTiquete.setPrioridad(prioridad);
        nuevoTiquete.setCategoria(categoria);
        nuevoTiquete.setEstado("abierto");
        nuevoTiquete.setCreadorId(usuario.getId());
        
        try {
            boolean creado = tiqueteDAO.crear(nuevoTiquete);
            
            if (creado) {
                JOptionPane.showMessageDialog(this,
                    "Tiquete creado exitosamente.\nID: IT" + nuevoTiquete.getId(),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al crear el tiquete",
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
