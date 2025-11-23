
package com.mycompany.tcketsproject;

public class Tiquete {
    private int id;
    private String titulo;
    private String descripcion;
    private String prioridad;
    private String categoria;
    private String estado; // abierto, en_progreso, resuelto, cerrado

    private Usuario creador;
    private Usuario asignado;

    private Chat chat;

    public Tiquete(int id, String titulo, String descripcion, String prioridad, String categoria, Usuario creador) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.categoria = categoria;
        this.estado = "abierto";
        this.creador = creador;
        this.chat = new Chat();
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getPrioridad() { return prioridad; }
    public String getCategoria() { return categoria; }
    public String getEstado() { return estado; }
    public Usuario getCreador() { return creador; }
    public Usuario getAsignado() { return asignado; }
    public Chat getChat() { return chat; }

    public void setEstado(String estado) { this.estado = estado; }
    public void setAsignado(Usuario asignado) { this.asignado = asignado; }
}