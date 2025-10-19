
package com.mycompany.tcketsproject;


public class Usuario {
    private int idUsuario;
    private String name;
    private String email;
    private String labor;

    public Usuario(int idUsuario, String name, String email, String labor) {
        this.idUsuario = idUsuario;
        this.name = name;
        this.email = email;
        this.labor = labor;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLabor() {
        return labor;
    }

    public void setLabor(String labor) {
        this.labor = labor;
    }
    
    
    
}
