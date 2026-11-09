package com.ouvidoria.service;

import com.ouvidoria.entity.OrdemServico;
import com.ouvidoria.entity.OrgaoResponsavel;
import com.ouvidoria.entity.Reclamacao;
import com.ouvidoria.entity.TecnicoCampo;
import com.ouvidoria.repository.OrdemServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdemServicoService {

    @Autowired
    private OrdemServicoRepository dao;

    @Autowired
    private ReclamacaoService reclamacaoService;

    @Autowired
    private OrgaoResponsavelService orgaoService;

    @Autowired
    private TecnicoCampoService tecnicoService;

    public OrdemServico abrirParaReclamacao(Long idReclamacao, Long idOrgao) {
        Reclamacao r = reclamacaoService.buscarPorId(idReclamacao);
        OrgaoResponsavel o = orgaoService.buscarPorId(idOrgao);
        OrdemServico os = o.abrirOrdemServico(r);
        return dao.save(os);
    }

    public OrdemServico atribuirTecnico(Long idOrdemServico, Long idTecnico) {
        OrdemServico os = buscarPorId(idOrdemServico);
        TecnicoCampo t = tecnicoService.buscarPorId(idTecnico);
        t.iniciarServico(os);
        return dao.save(os);
    }

    public OrdemServico concluir(Long idOrdemServico, String relatorio, double custoReal) {
        OrdemServico os = buscarPorId(idOrdemServico);
        os.concluir(relatorio);
        os.setCustoReal(custoReal);
        return dao.save(os);
    }

    public OrdemServico buscarPorId(Long id) {
        return dao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de servico nao encontrada: id " + id));
    }

    public List<OrdemServico> listarTodos() {
        return dao.findAll();
    }
}
