/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import com.mycompany.tcketsproject.Notificacion;
import com.mycompany.tcketsproject.Usuario;
import java.util.ArrayList;


public class NotificacionManager {
    private ArrayList<Notificacion> notificaciones;


    public NotificacionManager() {
        notificaciones = new ArrayList<>();
    }


    // Enviar una notificación
    public void enviarNotificacion(Notificacion n) {
        notificaciones.add(n);
    }


    // Listar notificaciones de un usuario
    public ArrayList<Notificacion> obtenerNotificacionesDe(Usuario u) {
        ArrayList<Notificacion> resultado = new ArrayList<>();
        for (Notificacion n : notificaciones) {
            if (n.getDestinatario().equals(u)) {
                resultado.add(n);
            }
        }
    return resultado;
    }


    // Marcar todas las notificaciones de un usuario como leídas
    public void marcarTodasComoLeidas(Usuario u) {
        for (Notificacion n : notificaciones) {
            if (n.getDestinatario().equals(u)) {
                n.marcarComoLeida();
            }
        }
    }


    public ArrayList<Notificacion> getNotificaciones() {
        return notificaciones;
    }
}