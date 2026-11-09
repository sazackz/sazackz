package com.ouvidoria.repository;

import com.ouvidoria.entity.Reclamacao;
import com.ouvidoria.enums.StatusReclamacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReclamacaoRepository extends JpaRepository<Reclamacao, Long> {
    Optional<Reclamacao> findByProtocolo(String protocolo);

    List<Reclamacao> findByCidadaoId(Long idCidadao);

    List<Reclamacao> findByEnderecoBairro(String bairro);

    List<Reclamacao> findByStatusNotInAndPrazoLimiteBefore(List<StatusReclamacao> statusFinalizados, LocalDateTime agora);
}
