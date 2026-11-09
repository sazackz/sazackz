package com.ouvidoria.repository;

import com.ouvidoria.entity.TecnicoCampo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TecnicoCampoRepository extends JpaRepository<TecnicoCampo, Long> {
    List<TecnicoCampo> findByDisponivelTrue();
}
