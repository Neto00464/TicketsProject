
package com.mycompany.tcketsproject;

public class Tiquete {
    private int IdTiquete;
    private String Categoria, Titulo, Prioridad, Estado;

    public Tiquete(int IdTiquete, String Categoria, String Titulo, String Prioridad, String Estado) {
        this.IdTiquete = IdTiquete;
        this.Categoria = Categoria;
        this.Titulo = Titulo;
        this.Prioridad = Prioridad;
        this.Estado = Estado;
    }

    public int getIdTiquete() {
        return IdTiquete;
    }

    public void setIdTiquete(int IdTiquete) {
        this.IdTiquete = IdTiquete;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String Categoria) {
        this.Categoria = Categoria;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String Titulo) {
        this.Titulo = Titulo;
    }

    public String getPrioridad() {
        return Prioridad;
    }

    public void setPrioridad(String Prioridad) {
        this.Prioridad = Prioridad;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }
    
    
    
    
}
