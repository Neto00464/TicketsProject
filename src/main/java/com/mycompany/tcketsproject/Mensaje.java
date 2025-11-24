
package com.mycompany.tcketsproject;

import java.time.LocalDateTime;

public class Mensaje {

    private Usuario autor;
    private String contenido;
    private LocalDateTime fecha;

    public Mensaje(Usuario autor, String contenido) {
        this.autor = autor;
        this.contenido = contenido;
        this.fecha = LocalDateTime.now();
    }

    public Usuario getAutor() {
        return autor;
    }

    public String getContenido() {
        return contenido;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
