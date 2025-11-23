/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tcketsproject;

/**
 *
 * @author Neto
 */
public class Notificacion {
private String mensaje;
private Usuario destinatario;
private boolean leida;


public Notificacion(String mensaje, Usuario destinatario) {
this.mensaje = mensaje;
this.destinatario = destinatario;
this.leida = false;
}


public String getMensaje() { return mensaje; }
public Usuario getDestinatario() { return destinatario; }
public boolean isLeida() { return leida; }


public void marcarComoLeida() {
this.leida = true;
}
}