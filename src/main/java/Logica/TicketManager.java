/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import com.mycompany.tcketsproject.Tiquete;
import com.mycompany.tcketsproject.Usuario;
import java.util.ArrayList;


public class TicketManager {


private ArrayList<Tiquete> tiquetes;


public TicketManager() {
tiquetes = new ArrayList<>();
}


// Crear un nuevo tiquete
public void crearTiquete(Tiquete t) {
tiquetes.add(t);
}


// Asignar técnico
public void asignarTiquete(int idTiquete, Usuario tecnico) {
Tiquete t = buscarTiquetePorId(idTiquete);
if (t != null) {
t.setAsignado(tecnico);
}
}


// Cambiar estado de un tiquete
public void cambiarEstado(int idTiquete, String nuevoEstado) {
Tiquete t = buscarTiquetePorId(idTiquete);
if (t != null) {
t.setEstado(nuevoEstado);
}
}


// Buscar un tiquete por ID
public Tiquete buscarTiquetePorId(int id) {
for (Tiquete t : tiquetes) {
if (t.getId() == id) return t;
}
return null;
}


// Listar todos los tiquetes
public ArrayList<Tiquete> getTiquetes() {
return tiquetes;
}
}
