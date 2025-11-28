package Model;

import java.time.LocalDate;

public class Usuario {
    private int id;
    private String nombre;
    private String correo;
    private String password;
    private String rol; // empleado, tecnico, admin
    private LocalDate fechaNacimiento;
    private String cedula;
    
    public Usuario() {}
    
    public Usuario(int id, String nombre, String correo, String password, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
    }
    
    public Usuario(int id, String nombre, String correo, String password, String rol, 
                   LocalDate fechaNacimiento, String cedula) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
        this.fechaNacimiento = fechaNacimiento;
        this.cedula = cedula;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { 
        this.fechaNacimiento = fechaNacimiento; 
    }
    
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    
    @Override
    public String toString() {
        return nombre + " (" + rol + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return id == usuario.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}