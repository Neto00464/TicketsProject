package Model;

import java.time.LocalDateTime;

public class Tiquete {
    private int id;
    private String titulo;
    private String descripcion;
    private String prioridad; // baja, media, alta, urgente
    private String categoria;
    private String estado; // abierto, en_progreso, resuelto, cerrado
    private int creadorId;
    private String creadorNombre;
    private Integer asignadoId;
    private String asignadoNombre;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    public Tiquete() {}
    
    public Tiquete(int id, String titulo, String descripcion, String prioridad, 
                   String categoria, String estado, int creadorId) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.categoria = categoria;
        this.estado = estado;
        this.creadorId = creadorId;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public int getCreadorId() { return creadorId; }
    public void setCreadorId(int creadorId) { this.creadorId = creadorId; }
    
    public String getCreadorNombre() { return creadorNombre; }
    public void setCreadorNombre(String creadorNombre) { 
        this.creadorNombre = creadorNombre; 
    }
    
    public Integer getAsignadoId() { return asignadoId; }
    public void setAsignadoId(Integer asignadoId) { this.asignadoId = asignadoId; }
    
    public String getAsignadoNombre() { return asignadoNombre; }
    public void setAsignadoNombre(String asignadoNombre) { 
        this.asignadoNombre = asignadoNombre; 
    }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { 
        this.fechaCreacion = fechaCreacion; 
    }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { 
        this.fechaActualizacion = fechaActualizacion; 
    }
    
    @Override
    public String toString() {
        return "Tiquete #" + id + " - " + titulo;
    }
}
