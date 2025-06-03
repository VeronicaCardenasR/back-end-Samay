package com.example.samay.ventaProducto.controller;

import com.example.samay.ventaProducto.service.VentaProductoService;
import com.example.samay.ventaProducto.model.VentaProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venta-productos")
@CrossOrigin(origins = "*")
public class VentaProductoController {

    private final VentaProductoService ventaProductoService;

    @Autowired
    public VentaProductoController(VentaProductoService ventaProductoService) {
        this.ventaProductoService = ventaProductoService;
    }

    @GetMapping
    public List<VentaProducto> listarVentas() {
        return ventaProductoService.obtenerTodas();
    }

    @GetMapping ("/buscar/{id}")
    public VentaProducto obtenerPorId(@PathVariable Long id) {
        return ventaProductoService.obtenerPorId(id);
    }

    @PostMapping("/agregarVentaProducto")
    public ResponseEntity<String> guardarVentaProducto(@RequestBody VentaProducto ventaProducto) {
        ventaProductoService.guardarVentaProducto(ventaProducto);
        return ResponseEntity.ok("Producto agregado a la venta correctamente");
    }


}


