package com.example.samay.usuario.service;

import com.example.samay.usuario.model.Usuario;
import com.example.samay.usuario.repository.IusuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UsuarioService implements IusuarioService {

    private final IusuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(IusuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public void guardarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);

    }

    @Override
    public void deleteUsuario(Long id) {

        usuarioRepository.deleteById(id);

    }

    @Override
    public void editarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado con el id: " + id));

        if (usuarioActualizado.getNombre() != null && !usuarioActualizado.getNombre().isBlank()) {
            usuarioExistente.setNombre(usuarioActualizado.getNombre());
        }

        if (usuarioActualizado.getTelefono() != null && !usuarioActualizado.getTelefono().isBlank()) {
            usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
        }

        if (usuarioActualizado.getContrasena() != null && !usuarioActualizado.getContrasena().isBlank()) {
            usuarioExistente.setContrasena(usuarioActualizado.getContrasena());
        }

        if (usuarioActualizado.getRol() != null && !usuarioActualizado.getRol().isBlank()) {
            usuarioExistente.setRol(usuarioActualizado.getRol());
        }

        usuarioRepository.save(usuarioExistente);

    }
}
