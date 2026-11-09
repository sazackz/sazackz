package com.ouvidoria.entity;

import com.ouvidoria.enums.Prioridade;
import com.ouvidoria.enums.StatusReclamacao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "reclamacao")
public class Reclamacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String protocolo;
    private String titulo;
    private String descricao;
    private LocalDateTime dataAbertura;
    private LocalDateTime prazoLimite;

    @Enumerated(EnumType.STRING)
    private StatusReclamacao status;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    private int notaAvaliacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cidadao")
    @JsonIgnoreProperties({"senha"})
    private Cidadao cidadao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private CategoriaProblema categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orgao_responsavel")
    private OrgaoResponsavel orgaoResponsavel;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_endereco")
    private Endereco endereco;

    @OneToMany(mappedBy = "reclamacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Anexo> anexos = new ArrayList<>();

    @OneToMany(mappedBy = "reclamacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoStatus> historico = new ArrayList<>();

    public Reclamacao() {
    }

    public void abrir() {
        this.protocolo = gerarProtocolo();
        this.dataAbertura = LocalDateTime.now();
        this.status = StatusReclamacao.ABERTA;
        if (this.prioridade == null) {
            this.prioridade = calcularPrioridade();
        }
    }

    public void alterarStatus(StatusReclamacao novo, Servidor s) {
        HistoricoStatus h = new HistoricoStatus();
        h.setStatusAnterior(this.status);
        h.setStatusNovo(novo);
        h.setDataHora(LocalDateTime.now());
        h.setReclamacao(this);
        this.historico.add(h);
        this.status = novo;
    }

    public void cancelar(String motivo) {
        this.status = StatusReclamacao.CANCELADA;
    }

    public void reabrir(String motivo) {
        this.status = StatusReclamacao.REABERTA;
    }

    public boolean estaAtrasada() {
        return prazoLimite != null
                && LocalDateTime.now().isAfter(prazoLimite)
                && status != StatusReclamacao.RESOLVIDA
                && status != StatusReclamacao.FECHADA
                && status != StatusReclamacao.CANCELADA;
    }

    public void avaliar(int nota) {
        this.notaAvaliacao = nota;
    }

    public String gerarProtocolo() {
        return "OUV-" + LocalDateTime.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public Prioridade calcularPrioridade() {
        if (categoria != null && categoria.getPrioridadePadrao() != null) {
            return categoria.getPrioridadePadrao();
        }
        return Prioridade.MEDIA;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getPrazoLimite() {
        return prazoLimite;
    }

    public void setPrazoLimite(LocalDateTime prazoLimite) {
        this.prazoLimite = prazoLimite;
    }

    public StatusReclamacao getStatus() {
        return status;
    }

    public void setStatus(StatusReclamacao status) {
        this.status = status;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public int getNotaAvaliacao() {
        return notaAvaliacao;
    }

    public void setNotaAvaliacao(int notaAvaliacao) {
        this.notaAvaliacao = notaAvaliacao;
    }

    public Cidadao getCidadao() {
        return cidadao;
    }

    public void setCidadao(Cidadao cidadao) {
        this.cidadao = cidadao;
    }

    public CategoriaProblema getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaProblema categoria) {
        this.categoria = categoria;
    }

    public OrgaoResponsavel getOrgaoResponsavel() {
        return orgaoResponsavel;
    }

    public void setOrgaoResponsavel(OrgaoResponsavel orgaoResponsavel) {
        this.orgaoResponsavel = orgaoResponsavel;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<Anexo> getAnexos() {
        return anexos;
    }

    public void setAnexos(List<Anexo> anexos) {
        this.anexos = anexos;
    }

    public List<HistoricoStatus> getHistorico() {
        return historico;
    }

    public void setHistorico(List<HistoricoStatus> historico) {
        this.historico = historico;
    }
}
