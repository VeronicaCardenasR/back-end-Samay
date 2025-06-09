package com.example.samay.usuario.controller;

import com.example.samay.JwtUtil;
import com.example.samay.usuario.model.Usuario;
import com.example.samay.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {


    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> listaUsuarios(){
        return usuarioService.obtenerTodos();

    }

    @PostMapping("/agregarUsuario")
    public ResponseEntity<String> guardarUsuario(@RequestBody Usuario usuario) {
        usuarioService.guardarUsuario(usuario);
        return ResponseEntity.ok("Usuario agregado con éxito");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario user) {
        UserDetails userDetails = usuarioService.loadUserByUsername(user.getCorreo());
        if (userDetails != null && passwordEncoder.matches(user.getContrasena(), userDetails.getPassword())) {
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("");

            Usuario usuarioEntity = usuarioService.obtenerPorCorreo(user.getCorreo());
            String token = jwtUtil.generateToken(usuarioEntity.getUsuario_id(), userDetails.getUsername(), role);

            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("Credenciales inválidas");
    }
    @GetMapping ("/buscar/{id}")
    public Usuario obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);

    }
    @GetMapping("/buscarPorCorreo/{correo}")
    public ResponseEntity<Usuario> buscarPorCorreo(@PathVariable String correo) {
        Usuario usuario = usuarioService.obtenerPorCorreo(correo);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }



    @DeleteMapping ("/borrarUsuario/{id}")
    public ResponseEntity<String> deleteUsuario (@PathVariable Long id){
        usuarioService.deleteUsuario(id);
        return ResponseEntity.ok("Usuario eliminado con éxito");
    }

    @PutMapping("/editarUsuario/{id}")
    public ResponseEntity<String> editarUsuario (@PathVariable Long id, @RequestBody Usuario usuarioActualizado ){
        usuarioService.editarUsuario(id,usuarioActualizado);
        return ResponseEntity.ok("Usuario actualizado con éxito");
    }


}
