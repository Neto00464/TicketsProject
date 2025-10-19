
package com.mycompany.tcketsproject;

import java.util.Date;

public class Mensaje {
    private int IdMensaje;
    private String idTiquete;
    private Date FechaActual;
    private String DescripcionMensaje;

    public Mensaje() {
    }
    
    public Mensaje(int IdMensaje, String idTiquete, Date FechaActual, String DescripcionMensaje) {
        this.IdMensaje = IdMensaje;
        this.idTiquete = idTiquete;
        this.FechaActual = FechaActual;
        this.DescripcionMensaje = DescripcionMensaje;
    }

    public int getIdMensaje() {
        return IdMensaje;
    }

    public void setIdMensaje(int IdMensaje) {
        this.IdMensaje = IdMensaje;
    }

    public String getidTiquete() {
        return idTiquete;
    }

    public void setidTiquete(String idTiquete) {
        this.idTiquete = idTiquete;
    }

    public Date getFechaActual() {
        return FechaActual;
    }

    public void setFechaActual(Date FechaActual) {
        this.FechaActual = FechaActual;
    }

    public String getDescripcionMensaje() {
        return DescripcionMensaje;
    }

    public void setDescripcionMensaje(String DescripcionMensaje) {
        this.DescripcionMensaje = DescripcionMensaje;
    }
  
}
