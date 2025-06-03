package com.example.samay.producto.service;

import com.example.samay.producto.model.Producto;
import com.example.samay.producto.repository.IproductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService implements IproductoService {

    private final IproductoRepository productoRepository;

    @Autowired
    public ProductoService(IproductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Override
    public void guardarProducto(Producto producto) {
        productoRepository.save(producto);
    }

    @Override
    public void deleteProducto(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public void editarProducto(Long id, Producto productoActualizado) {
        Producto productoExistente = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado con el id: " + id));

        if (productoActualizado.getProductName() != null && !productoActualizado.getProductName().isBlank()) {
            productoExistente.setProductName(productoActualizado.getProductName());
        }

        if (productoActualizado.getCategory() != null) {
            productoExistente.setCategory(productoActualizado.getCategory());
        }

        if (productoActualizado.getRegion() != null && !productoActualizado.getRegion().isBlank()) {
            productoExistente.setRegion(productoActualizado.getRegion());
        }

        if (productoActualizado.getCommunity() != null && !productoActualizado.getCommunity().isBlank()){
            productoExistente.setCommunity(productoActualizado.getCommunity());
        }

        if (productoActualizado.getDescription() != null && !productoActualizado.getDescription().isBlank()) {
            productoExistente.setDescription(productoActualizado.getDescription());
        }
        if (productoActualizado.getPrice() != null) {
            productoExistente.setPrice(productoActualizado.getPrice());
        }
        if (productoActualizado.getQuanty() != null) {
            productoExistente.setQuanty(productoActualizado.getQuanty());
        }
        if (productoActualizado.getImg() != null && !productoActualizado.getImg().isBlank()) {
            productoExistente.setImg(productoActualizado.getImg());
        }


        productoRepository.save(productoExistente);
    }


}
