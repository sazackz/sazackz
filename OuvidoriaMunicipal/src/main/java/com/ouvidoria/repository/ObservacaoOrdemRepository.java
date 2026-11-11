package com.ouvidoria.repository;

import com.ouvidoria.entity.ObservacaoOrdem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObservacaoOrdemRepository extends JpaRepository<ObservacaoOrdem, Long> {

    List<ObservacaoOrdem> findByOrdemServicoIdOrderByDataHoraAsc(Long idOrdemServico);

    long countByOrdemServicoId(Long idOrdemServico);
}
