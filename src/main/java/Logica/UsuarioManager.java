/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import com.mycompany.tcketsproject.Usuario;
import java.util.ArrayList;


public class UsuarioManager {
    private ArrayList<Usuario> usuarios;


    public UsuarioManager() {
        usuarios = new ArrayList<>();
    }


    public void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }


    public Usuario login(String correo, String password) {
        for (Usuario u : usuarios) {
            if (u.getCorreo().equals(correo) && u.getPassword().equals(password)) {
                return u;
            }
        }
    return null; // login fallido
    }


    public Usuario buscarUsuarioPorId(int id) {
        for (Usuario u : usuarios) {
            if (u.getId() == id) {
                return u;
            }
        }
    return null;
    }


    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }
    
    public Usuario buscarUsuarioPorCorreo(String correo) {
    for (Usuario u : usuarios) {
        if (u.getCorreo().equalsIgnoreCase(correo)) {
            return u;
        }
    }
    return null;
}

public boolean validarLogin(String correo, String password) {
    Usuario user = buscarUsuarioPorCorreo(correo);
    if (user == null) return false;
    return user.getPassword().equals(password);
}

}
