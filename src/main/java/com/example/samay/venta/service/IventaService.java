package com.example.samay.venta.service;

import com.example.samay.venta.model.Venta;

import java.util.List;

public interface IventaService {
    List<Venta> obtenerTodas();
    Venta obtenerPorId(Long id);
    void guardarVenta(Venta venta);


}
