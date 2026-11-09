package com.ouvidoria.repository;

import com.ouvidoria.entity.Servidor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServidorRepository extends JpaRepository<Servidor, Long> {
    Optional<Servidor> findByEmail(String email);
}
