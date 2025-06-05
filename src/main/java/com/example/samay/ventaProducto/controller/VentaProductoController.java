package com.example.samay.ventaProducto.controller;

import com.example.samay.ventaProducto.model.VentaConProductosDTO;
import com.example.samay.ventaProducto.service.VentaProductoService;
import com.example.samay.ventaProducto.model.VentaProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/buscar/{id}")
    public VentaProducto obtenerPorId(@PathVariable Long id) {
        return ventaProductoService.obtenerPorId(id);
    }

    @PostMapping("/agregarVentaProducto")
    public ResponseEntity<Map<String, String>> guardarVentaProducto(@RequestBody VentaProducto ventaProducto) {
        try {
            ventaProductoService.guardarVentaProducto(ventaProducto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Producto agregado a la venta correctamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }


    @PostMapping("/agregar-multiples")
    public ResponseEntity<Map<String, String>> agregarProductosAVenta(@RequestBody VentaConProductosDTO dto) {
        try {
            ventaProductoService.guardarVentaConProductos(dto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Productos agregados a la venta correctamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }



    @GetMapping("/venta/{ventaId}")
    public List<VentaProducto> obtenerPorVenta(@PathVariable Long ventaId) {
        return ventaProductoService.obtenerPorVentaId(ventaId);





    }

}




