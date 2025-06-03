package com.example.samay.usuario.repository;

import com.example.samay.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IusuarioRepository extends JpaRepository<Usuario, Long> {
}
