/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import com.mycompany.tcketsproject.Mensaje;
import com.mycompany.tcketsproject.Notificacion;
import com.mycompany.tcketsproject.Tiquete;
import com.mycompany.tcketsproject.Usuario;

import java.util.ArrayList;
    public class BaseDatos {
    private ArrayList<Usuario> usuarios;
    private ArrayList<Tiquete> tiquetes;
    private ArrayList<Mensaje> mensajes;
    private ArrayList<Notificacion> notificaciones;


    public BaseDatos() {
        usuarios = new ArrayList<>();
        tiquetes = new ArrayList<>();
        mensajes = new ArrayList<>();
        notificaciones = new ArrayList<>();
    }


    // Métodos para usuarios
    public void agregarUsuario(Usuario u) { usuarios.add(u); }
    public ArrayList<Usuario> getUsuarios() { return usuarios; }


    // Métodos para tiquetes
    public void agregarTiquete(Tiquete t) { tiquetes.add(t); }
    public ArrayList<Tiquete> getTiquetes() { return tiquetes; }


    // Métodos para mensajes
    public void agregarMensaje(Mensaje m) { mensajes.add(m); }
    public ArrayList<Mensaje> getMensajes() { return mensajes; }


    // Métodos para notificaciones
    public void agregarNotificacion(Notificacion n) { notificaciones.add(n); }
    public ArrayList<Notificacion> getNotificaciones() { return notificaciones; }
    }
