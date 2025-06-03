package com.example.samay.ventaProducto.service;

import com.example.samay.ventaProducto.model.VentaProducto;

import java.util.List;

public interface IventaProductoService {

    List<VentaProducto> obtenerTodas();
    VentaProducto obtenerPorId(Long id);
    void guardarVentaProducto(VentaProducto ventaProducto);

}
