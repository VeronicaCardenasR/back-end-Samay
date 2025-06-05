package com.example.samay.ventaProducto.service;

import com.example.samay.ventaProducto.model.VentaProducto;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IventaProductoService {

    List<VentaProducto> obtenerTodas();
    VentaProducto obtenerPorId(Long id);
    void guardarVentaProducto(VentaProducto ventaProducto);
    List<VentaProducto> obtenerPorVentaId(Long ventaId);

}
