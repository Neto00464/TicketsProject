package com.mycompany.tcketsproject;


public class Usuario {
private int id;
private String nombre;
private String correo;
private String password;
private String rol; // empleado, tecnico, admin


public Usuario(int id, String nombre, String correo, String password, String rol) {
this.id = id;
this.nombre = nombre;
this.correo = correo;
this.password = password;
this.rol = rol;
}


public int getId() { return id; }
public String getNombre() { return nombre; }
public String getCorreo() { return correo; }
public String getPassword() { return password; }
public String getRol() { return rol; }


public void setNombre(String nombre) { this.nombre = nombre; }
public void setCorreo(String correo) { this.correo = correo; }
public void setPassword(String password) { this.password = password; }
public void setRol(String rol) { this.rol = rol; }
}