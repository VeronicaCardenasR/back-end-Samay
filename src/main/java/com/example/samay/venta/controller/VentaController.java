package com.example.samay.venta.controller;

import com.example.samay.venta.service.VentaService;
import com.example.samay.venta.model.Venta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<String> guardarVenta(@RequestBody Venta venta) {
        ventaService.guardarVenta(venta);
        return ResponseEntity.ok("Venta agregada con éxito");
    }





}
