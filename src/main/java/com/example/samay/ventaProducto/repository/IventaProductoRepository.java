package com.example.samay.ventaProducto.repository;

import com.example.samay.ventaProducto.model.VentaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IventaProductoRepository extends JpaRepository<VentaProducto, Long> {
//    List<VentaProducto> findByVenta_VentaId(Long ventaId);

}
