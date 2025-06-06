package com.example.samay.usuario.controller;

public class LoginResponse {
    private String token;
    private String rol;
    private String nombre;
    private String correo;

    public LoginResponse(String token, String rol, String nombre, String correo) {
        this.token = token;
        this.rol = rol;
        this.nombre = nombre;
        this.correo = correo;
    }

    // Getters y setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}
