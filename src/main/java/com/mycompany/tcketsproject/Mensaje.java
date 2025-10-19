
package com.mycompany.tcketsproject;

import java.util.Date;

public class Mensaje {
    private int IdMensaje;
    private String idChat;
    private Date FechaActual;
    private String DescripcionMensaje;

    public Mensaje(int IdMensaje, String idChat, Date FechaActual, String DescripcionMensaje) {
        this.IdMensaje = IdMensaje;
        this.idChat = idChat;
        this.FechaActual = FechaActual;
        this.DescripcionMensaje = DescripcionMensaje;
    }

    public int getIdMensaje() {
        return IdMensaje;
    }

    public void setIdMensaje(int IdMensaje) {
        this.IdMensaje = IdMensaje;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
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
