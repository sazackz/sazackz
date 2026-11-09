package com.ouvidoria.service;

import com.ouvidoria.entity.CategoriaProblema;
import com.ouvidoria.entity.Cidadao;
import com.ouvidoria.entity.OrgaoResponsavel;
import com.ouvidoria.entity.Reclamacao;
import com.ouvidoria.enums.StatusReclamacao;
import com.ouvidoria.repository.ReclamacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReclamacaoService {

    private static final List<StatusReclamacao> STATUS_FINALIZADOS = List.of(
            StatusReclamacao.RESOLVIDA, StatusReclamacao.FECHADA, StatusReclamacao.CANCELADA
    );

    @Autowired
    private ReclamacaoRepository dao;

    @Autowired
    private CidadaoService cidServ;

    @Autowired
    private CategoriaProblemaService categoriaServ;

    @Autowired
    private OrgaoResponsavelService orgaoServ;

    public Reclamacao abrir(Reclamacao r) {
        if (r.getCidadao() != null && r.getCidadao().getId() != null) {
            Cidadao cidadao = cidServ.buscarPorId(r.getCidadao().getId());
            r.setCidadao(cidadao);
        }
        if (r.getCategoria() != null && r.getCategoria().getId() != null) {
            CategoriaProblema categoria = categoriaServ.buscarPorId(r.getCategoria().getId());
            r.setCategoria(categoria);
        }
        if (r.getOrgaoResponsavel() != null && r.getOrgaoResponsavel().getId() != null) {
            OrgaoResponsavel orgao = orgaoServ.buscarPorId(r.getOrgaoResponsavel().getId());
            r.setOrgaoResponsavel(orgao);
        }
        if (verificarDuplicidade(r)) {
            throw new IllegalArgumentException("Ja existe uma reclamacao aberta muito parecida com essa");
        }
        r.abrir();
        r.setPrazoLimite(calcularPrazo(r));
        if (r.getEndereco() != null) {
            r.getEndereco().setId(null);
        }
        return dao.save(r);
    }

    public boolean verificarDuplicidade(Reclamacao r) {
        if (r.getCidadao() == null || r.getCidadao().getId() == null) {
            return false;
        }
        return dao.findByCidadaoId(r.getCidadao().getId()).stream()
                .anyMatch(existente -> existente.getTitulo() != null
                        && existente.getTitulo().equalsIgnoreCase(r.getTitulo())
                        && !STATUS_FINALIZADOS.contains(existente.getStatus()));
    }

    public boolean validarTransicao(StatusReclamacao novo) {
        return novo != null;
    }

    public LocalDateTime calcularPrazo(Reclamacao r) {
        if (r.getCategoria() != null) {
            return r.getCategoria().calcularPrazo(r.calcularPrioridade());
        }
        return LocalDateTime.now().plusHours(72);
    }

    public Reclamacao buscarPorId(Long id) {
        return dao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reclamacao nao encontrada: id " + id));
    }

    public Reclamacao buscarPorProtocolo(String protocolo) {
        return dao.findByProtocolo(protocolo)
                .orElseThrow(() -> new IllegalArgumentException("Reclamacao nao encontrada: protocolo " + protocolo));
    }

    public List<Reclamacao> listarTodos() {
        return dao.findAll();
    }

    public List<Reclamacao> listarMinhasReclamacoes(Long idCidadao) {
        return dao.findByCidadaoId(idCidadao);
    }

    public List<Reclamacao> listarPorBairro(String bairro) {
        return dao.findByEnderecoBairro(bairro);
    }

    public void alterarStatus(Long id, StatusReclamacao novo) {
        if (!validarTransicao(novo)) {
            throw new IllegalArgumentException("Transicao de status invalida");
        }
        Reclamacao r = buscarPorId(id);
        r.alterarStatus(novo, null);
        dao.save(r);
    }

    public void cancelar(Long id, String motivo) {
        Reclamacao r = buscarPorId(id);
        r.cancelar(motivo);
        dao.save(r);
    }

    public void reabrir(Long id, String motivo) {
        Reclamacao r = buscarPorId(id);
        r.reabrir(motivo);
        dao.save(r);
    }

    public void avaliar(Long id, int nota) {
        Reclamacao r = buscarPorId(id);
        r.avaliar(nota);
        dao.save(r);
    }

    public List<Reclamacao> escalonarAtrasadas() {
        List<Reclamacao> atrasadas = dao.findByStatusNotInAndPrazoLimiteBefore(STATUS_FINALIZADOS, LocalDateTime.now());
        for (Reclamacao r : atrasadas) {
            if (r.getPrioridade() != com.ouvidoria.enums.Prioridade.URGENTE) {
                r.setPrioridade(com.ouvidoria.enums.Prioridade.URGENTE);
                dao.save(r);
            }
        }
        return atrasadas;
    }
}
