package com.example.samay.producto.controller;

import com.example.samay.producto.service.ProductoService;
import com.example.samay.producto.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService productoService;
    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> listaProductos(){
        return productoService.obtenerTodos();

    }

    @GetMapping ("/buscar/{id}")
    public Producto obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id);

    }
    @PostMapping("/agregarProducto")
    public ResponseEntity<String> guardarProducto(@RequestBody Producto producto) {
        productoService.guardarProducto(producto);
        return ResponseEntity.ok("Producto agregado con éxito");
    }

    @DeleteMapping ("/borrarProducto/{id}")
    public ResponseEntity<String> deleteProducto (@PathVariable Long id){
        productoService.deleteProducto(id);
        return ResponseEntity.ok("Producto eliminado con éxito");
    }

    @PutMapping("/editarProducto/{id}")
    public ResponseEntity<String> editarProducto (@PathVariable Long id, @RequestBody Producto productoActualizado ){
        productoService.editarProducto(id,productoActualizado);
        return ResponseEntity.ok("Producto actualizado con éxito");
    }
}
