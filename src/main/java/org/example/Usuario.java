package org.example;

public class Usuario {
    Integer idUsuario;
    String usuario;
    String password;
    String nombre;
    String email;

    public Usuario(Integer idUsuario, String usuario, String password, String nombre, String email) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.password = password;
        this.nombre = nombre;
        this.email = email;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
