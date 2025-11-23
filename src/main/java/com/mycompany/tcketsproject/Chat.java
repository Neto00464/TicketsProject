/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tcketsproject;

import java.util.ArrayList;


public class Chat {
    private ArrayList<Mensaje> mensajes;


    public Chat() {
        this.mensajes = new ArrayList<>();
    }


    public void agregarMensaje(Mensaje m) {
        mensajes.add(m);
    }


    public ArrayList<Mensaje> getMensajes() {
        return mensajes;
    }
}