package com.example.samay.producto.service;


import com.example.samay.producto.model.Producto;

import java.util.List;

public interface IproductoService {

    List<Producto> obtenerTodos();
    Producto obtenerPorId(Long id);
    void guardarProducto(Producto producto);
    void deleteProducto(Long id);
    void editarProducto (Long id, Producto productoActualizado);
}
