package com.ouvidoria.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ouvidoria.enums.StatusReclamacao;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orgao_responsavel")
public class OrgaoResponsavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cnpj;
    private String telefone;
    private String email;
    private boolean ativo = true;

    @OneToMany(mappedBy = "orgaoResponsavel")
    @JsonIgnore
    private List<Reclamacao> reclamacoes = new ArrayList<>();

    public OrgaoResponsavel() {
    }

    public void receberReclamacao(Reclamacao r) {
        if (r != null) {
            r.setOrgaoResponsavel(this);
        }
    }

    public OrdemServico abrirOrdemServico(Reclamacao r) {
        OrdemServico os = new OrdemServico();
        os.setReclamacao(r);
        os.setOrgaoResponsavel(this);
        return os;
    }

    public List<Reclamacao> listarPendentes() {
        return reclamacoes.stream()
                .filter(r -> r.getStatus() != StatusReclamacao.RESOLVIDA
                        && r.getStatus() != StatusReclamacao.FECHADA
                        && r.getStatus() != StatusReclamacao.CANCELADA)
                .toList();
    }

    // media em horas entre a abertura e o registro de RESOLVIDA no historico
    public double calcularTempoMedioResolucao() {
        return reclamacoes.stream()
                .flatMap(r -> r.getHistorico().stream()
                        .filter(h -> h.getStatusNovo() == StatusReclamacao.RESOLVIDA)
                        .map(h -> Duration.between(r.getDataAbertura(), h.getDataHora()).toMinutes() / 60.0))
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
