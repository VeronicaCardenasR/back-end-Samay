package com.example.samay.usuario.service;

import com.example.samay.usuario.model.Usuario;

import java.util.List;

public interface IusuarioService {
    List<Usuario> obtenerTodos();
    Usuario obtenerPorId(Long id);
    void guardarUsuario(Usuario usuario);
    void deleteUsuario(Long id);
    void editarUsuario (Long id, Usuario usuarioActualizado);
}

