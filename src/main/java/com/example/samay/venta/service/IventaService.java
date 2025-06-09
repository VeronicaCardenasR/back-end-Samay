package com.example.samay.venta.service;

import com.example.samay.venta.model.Venta;

import java.util.List;

public interface IventaService {

    List<Venta> obtenerTodas();
    Venta obtenerPorId(Long id);
    Venta guardarVenta(Venta venta);
    void actualizarEstadoPago(Long ventaId, Venta estado);


}
