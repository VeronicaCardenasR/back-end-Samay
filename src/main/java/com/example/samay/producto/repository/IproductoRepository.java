package com.example.samay.producto.repository;

import com.example.samay.producto.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IproductoRepository extends JpaRepository<Producto, Long> {
}
