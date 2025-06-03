package com.example.samay.usuario.controller;

import com.example.samay.usuario.model.Usuario;
import com.example.samay.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

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

    @GetMapping ("/buscar/{id}")
    public Usuario obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);

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
