package com.example.samay.usuario.service;

import com.example.samay.usuario.model.Usuario;
import com.example.samay.usuario.repository.IusuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class UsuarioService implements IusuarioService {

    private final IusuarioRepository usuarioRepository;


    @Autowired
    public UsuarioService(IusuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario guardarUsuario(Usuario usuario) {
        Usuario userLogin = new Usuario();

        userLogin.setNombre(usuario.getNombre());
        userLogin.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        userLogin.setCorreo(usuario.getCorreo());
        userLogin.setTelefono(usuario.getTelefono());
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            userLogin.setRol("cliente"); // asigna rol por defecto si no viene
        } else {
            userLogin.setRol(usuario.getRol());
        }

        return usuarioRepository.save(userLogin);
    }
    // Métdo de carga de usuario implementado desde UserDetailsService
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = usuarioRepository.findByCorreo(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRol()));

        return new org.springframework.security.core.userdetails.User(
                user.getCorreo(),
                user.getContrasena(),
                authorities
        );
    }
    @Override
    public void deleteUsuario(Long id) {

        usuarioRepository.deleteById(id);

    }
    public Usuario obtenerPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
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
