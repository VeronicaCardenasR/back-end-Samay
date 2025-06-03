package com.example.samay.venta.repository;

import com.example.samay.venta.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IventaRepository extends JpaRepository<Venta, Long> {

}
