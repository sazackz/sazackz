package com.ouvidoria.repository;

import com.ouvidoria.entity.Cidadao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CidadaoRepository extends JpaRepository<Cidadao, Long> {
    Optional<Cidadao> findByCpf(String cpf);

    Optional<Cidadao> findByEmail(String email);
}
