package com.example.samay.usuario.model;

import jakarta.persistence.*;

@Entity
public class Usuario {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long usuario_id;

        @Column(nullable = false, length = 50)
        private String nombre;

        @Column(name = "contraseña", nullable = false, length = 255)
        private String contrasena;

        @Column(nullable = false, unique = true, length = 50)
        private String correo;

        @Column(nullable = false, length = 20)
        private String telefono;

        @Column(nullable = false)
        private String rol;

    public Usuario() {
    }

    public Usuario(Long usuario_id, String nombre, String contrasena, String correo, String telefono, String rol) {

        this.usuario_id = usuario_id;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.correo = correo;
        this.telefono = telefono;
        this.rol = rol;
    }

    public Long getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Long usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
