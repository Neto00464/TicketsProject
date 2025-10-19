
package com.mycompany.tcketsproject;


public class Usuario {
    private int IdUsuario;
    private String Name;
    private String Email;
    private String Labor;

    public Usuario(int IdUsuario, String Name, String Email, String Labor) {
        this.IdUsuario = IdUsuario;
        this.Name = Name;
        this.Email = Email;
        this.Labor = Labor;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getLabor() {
        return Labor;
    }

    public void setLabor(String Labor) {
        this.Labor = Labor;
    }
    
    

}
