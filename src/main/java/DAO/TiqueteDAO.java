package DAO;

import Database.DatabaseConnection;
import Model.Tiquete;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TiqueteDAO {
    
    public boolean crear(Tiquete tiquete) throws SQLException {
        String sql = "INSERT INTO tiquetes (titulo, descripcion, prioridad, categoria, " +
                     "estado, creador_id, asignado_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, tiquete.getTitulo());
            stmt.setString(2, tiquete.getDescripcion());
            stmt.setString(3, tiquete.getPrioridad());
            stmt.setString(4, tiquete.getCategoria());
            stmt.setString(5, tiquete.getEstado());
            stmt.setInt(6, tiquete.getCreadorId());
            
            if (tiquete.getAsignadoId() != null) {
                stmt.setInt(7, tiquete.getAsignadoId());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        tiquete.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public boolean actualizar(Tiquete tiquete) throws SQLException {
        String sql = "UPDATE tiquetes SET titulo = ?, descripcion = ?, prioridad = ?, " +
                     "categoria = ?, estado = ?, asignado_id = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tiquete.getTitulo());
            stmt.setString(2, tiquete.getDescripcion());
            stmt.setString(3, tiquete.getPrioridad());
            stmt.setString(4, tiquete.getCategoria());
            stmt.setString(5, tiquete.getEstado());
            
            if (tiquete.getAsignadoId() != null) {
                stmt.setInt(6, tiquete.getAsignadoId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            
            stmt.setInt(7, tiquete.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean cambiarEstado(int tiqueteId, String nuevoEstado) throws SQLException {
        String sql = "UPDATE tiquetes SET estado = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, tiqueteId);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean asignarTecnico(int tiqueteId, int tecnicoId) throws SQLException {
        String sql = "UPDATE tiquetes SET asignado_id = ?, estado = 'en_progreso' WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, tecnicoId);
            stmt.setInt(2, tiqueteId);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public Tiquete buscarPorId(int id) throws SQLException {
        String sql = "SELECT t.*, " +
                     "u1.nombre as creador_nombre, " +
                     "u2.nombre as asignado_nombre " +
                     "FROM tiquetes t " +
                     "INNER JOIN usuarios u1 ON t.creador_id = u1.id " +
                     "LEFT JOIN usuarios u2 ON t.asignado_id = u2.id " +
                     "WHERE t.id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTiquete(rs);
                }
            }
        }
        return null;
    }
    
    public List<Tiquete> obtenerPorCreador(int creadorId) throws SQLException {
        String sql = "SELECT t.*, " +
                     "u1.nombre as creador_nombre, " +
                     "u2.nombre as asignado_nombre " +
                     "FROM tiquetes t " +
                     "INNER JOIN usuarios u1 ON t.creador_id = u1.id " +
                     "LEFT JOIN usuarios u2 ON t.asignado_id = u2.id " +
                     "WHERE t.creador_id = ? " +
                     "ORDER BY t.fecha_creacion DESC";
        
        return ejecutarConsultaTiquetes(sql, creadorId);
    }
    
    public List<Tiquete> obtenerPorAsignado(int asignadoId) throws SQLException {
        String sql = "SELECT t.*, " +
                     "u1.nombre as creador_nombre, " +
                     "u2.nombre as asignado_nombre " +
                     "FROM tiquetes t " +
                     "INNER JOIN usuarios u1 ON t.creador_id = u1.id " +
                     "LEFT JOIN usuarios u2 ON t.asignado_id = u2.id " +
                     "WHERE t.asignado_id = ? " +
                     "ORDER BY t.fecha_creacion DESC";
        
        return ejecutarConsultaTiquetes(sql, asignadoId);
    }
    
    public List<Tiquete> obtenerTodos() throws SQLException {
        String sql = "SELECT t.*, " +
                     "u1.nombre as creador_nombre, " +
                     "u2.nombre as asignado_nombre " +
                     "FROM tiquetes t " +
                     "INNER JOIN usuarios u1 ON t.creador_id = u1.id " +
                     "LEFT JOIN usuarios u2 ON t.asignado_id = u2.id " +
                     "ORDER BY t.fecha_creacion DESC";
        
        List<Tiquete> tiquetes = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                tiquetes.add(mapResultSetToTiquete(rs));
            }
        }
        return tiquetes;
    }
    
    private List<Tiquete> ejecutarConsultaTiquetes(String sql, int parametro) throws SQLException {
        List<Tiquete> tiquetes = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, parametro);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tiquetes.add(mapResultSetToTiquete(rs));
                }
            }
        }
        return tiquetes;
    }
    
    private Tiquete mapResultSetToTiquete(ResultSet rs) throws SQLException {
        Tiquete tiquete = new Tiquete();
        tiquete.setId(rs.getInt("id"));
        tiquete.setTitulo(rs.getString("titulo"));
        tiquete.setDescripcion(rs.getString("descripcion"));
        tiquete.setPrioridad(rs.getString("prioridad"));
        tiquete.setCategoria(rs.getString("categoria"));
        tiquete.setEstado(rs.getString("estado"));
        tiquete.setCreadorId(rs.getInt("creador_id"));
        tiquete.setCreadorNombre(rs.getString("creador_nombre"));
        
        int asignadoId = rs.getInt("asignado_id");
        if (!rs.wasNull()) {
            tiquete.setAsignadoId(asignadoId);
            tiquete.setAsignadoNombre(rs.getString("asignado_nombre"));
        }
        
        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            tiquete.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }
        
        Timestamp fechaActualizacion = rs.getTimestamp("fecha_actualizacion");
        if (fechaActualizacion != null) {
            tiquete.setFechaActualizacion(fechaActualizacion.toLocalDateTime());
        }
        
        return tiquete;
    }
}