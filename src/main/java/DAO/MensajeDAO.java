/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MensajeDAO {
    
    public static class Mensaje {
        private int id;
        private int tiqueteId;
        private int usuarioId;
        private String usuarioNombre;
        private String contenido;
        private LocalDateTime fecha;
        
        public Mensaje() {}
        
        public Mensaje(int tiqueteId, int usuarioId, String usuarioNombre, String contenido) {
            this.tiqueteId = tiqueteId;
            this.usuarioId = usuarioId;
            this.usuarioNombre = usuarioNombre;
            this.contenido = contenido;
        }
        
        // Getters y Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public int getTiqueteId() { return tiqueteId; }
        public void setTiqueteId(int tiqueteId) { this.tiqueteId = tiqueteId; }
        
        public int getUsuarioId() { return usuarioId; }
        public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
        
        public String getUsuarioNombre() { return usuarioNombre; }
        public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }
        
        public String getContenido() { return contenido; }
        public void setContenido(String contenido) { this.contenido = contenido; }
        
        public LocalDateTime getFecha() { return fecha; }
        public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    }
    
    // Guardar un nuevo mensaje
    public boolean guardarMensaje(int tiqueteId, int usuarioId, String contenido) throws SQLException {
        String sql = "INSERT INTO mensajes (tiquete_id, usuario_id, contenido) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, tiqueteId);
            stmt.setInt(2, usuarioId);
            stmt.setString(3, contenido);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Obtener todos los mensajes de un tiquete
    public List<Mensaje> obtenerMensajesPorTiquete(int tiqueteId) throws SQLException {
        String sql = "SELECT m.*, u.nombre as usuario_nombre " +
                     "FROM mensajes m " +
                     "INNER JOIN usuarios u ON m.usuario_id = u.id " +
                     "WHERE m.tiquete_id = ? " +
                     "ORDER BY m.fecha ASC";
        
        List<Mensaje> mensajes = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, tiqueteId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Mensaje mensaje = new Mensaje();
                    mensaje.setId(rs.getInt("id"));
                    mensaje.setTiqueteId(rs.getInt("tiquete_id"));
                    mensaje.setUsuarioId(rs.getInt("usuario_id"));
                    mensaje.setUsuarioNombre(rs.getString("usuario_nombre"));
                    mensaje.setContenido(rs.getString("contenido"));
                    
                    Timestamp fecha = rs.getTimestamp("fecha");
                    if (fecha != null) {
                        mensaje.setFecha(fecha.toLocalDateTime());
                    }
                    
                    mensajes.add(mensaje);
                }
            }
        }
        
        return mensajes;
    }
    
    // Eliminar todos los mensajes de un tiquete
    public boolean eliminarMensajesPorTiquete(int tiqueteId) throws SQLException {
        String sql = "DELETE FROM mensajes WHERE tiquete_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, tiqueteId);
            return stmt.executeUpdate() > 0;
        }
    }
}