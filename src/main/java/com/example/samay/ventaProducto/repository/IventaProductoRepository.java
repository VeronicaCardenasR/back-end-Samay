package com.example.samay.ventaProducto.repository;

import com.example.samay.ventaProducto.model.VentaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IventaProductoRepository extends JpaRepository<VentaProducto, Long> {
}
