package com.example.samay.venta.controller;

import com.example.samay.venta.service.VentaService;
import com.example.samay.venta.model.Venta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    private final VentaService ventaService;

    @Autowired
    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public List<Venta> listarVentas() {
        return ventaService.obtenerTodas();
    }

    @GetMapping ("/buscar/{id}")
    public Venta obtenerPorId(@PathVariable Long id) {
        return ventaService.obtenerPorId(id);
    }

    @PostMapping("/agregarVenta")
    public ResponseEntity<Map<String, Object>> guardarVenta(@RequestBody Venta venta) {
        try {
            Venta ventaGuardada = ventaService.guardarVenta(venta);
            Map<String, Object> response = new HashMap<>();
            response.put("id", ventaGuardada.getVenta_id());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error al guardar venta: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al guardar la venta: " + e.getMessage());
            return ResponseEntity.status(400).body(errorResponse);
        }
    }


      @PutMapping("/actualizarEstado/{id}")

    public ResponseEntity<String> actualizarDatos(@PathVariable Long id, @RequestBody Venta venta){
        ventaService.actualizarEstadoPago(id,venta);
        return ResponseEntity.ok("Actualizado correctamente");

    }


}
