package com.ouvidoria.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ordem_servico")
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private LocalDateTime dataEmissao;
    private LocalDateTime dataConclusao;
    private String status;
    private double custoEstimado;
    private double custoReal;
    private String relatorioTecnico;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reclamacao")
    @JsonIgnoreProperties({"anexos", "historico"})
    private Reclamacao reclamacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orgao_responsavel")
    private OrgaoResponsavel orgaoResponsavel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tecnico")
    @JsonIgnoreProperties({"senha"})
    private TecnicoCampo tecnico;

    public OrdemServico() {
        this.numero = "OS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.dataEmissao = LocalDateTime.now();
        this.status = "ABERTA";
    }

    public void atribuirTecnico(TecnicoCampo t) {
        this.tecnico = t;
        this.status = "EM_EXECUCAO";
    }

    public void concluir(String relatorio) {
        this.relatorioTecnico = relatorio;
        this.dataConclusao = LocalDateTime.now();
        this.status = "CONCLUIDA";
    }

    public Duration calcularDuracao() {
        if (dataEmissao == null) {
            return Duration.ZERO;
        }
        LocalDateTime fim = dataConclusao != null ? dataConclusao : LocalDateTime.now();
        return Duration.between(dataEmissao, fim);
    }

    public boolean excedeuOrcamento() {
        return custoReal > custoEstimado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDateTime dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getCustoEstimado() {
        return custoEstimado;
    }

    public void setCustoEstimado(double custoEstimado) {
        this.custoEstimado = custoEstimado;
    }

    public double getCustoReal() {
        return custoReal;
    }

    public void setCustoReal(double custoReal) {
        this.custoReal = custoReal;
    }

    public String getRelatorioTecnico() {
        return relatorioTecnico;
    }

    public void setRelatorioTecnico(String relatorioTecnico) {
        this.relatorioTecnico = relatorioTecnico;
    }

    public Reclamacao getReclamacao() {
        return reclamacao;
    }

    public void setReclamacao(Reclamacao reclamacao) {
        this.reclamacao = reclamacao;
    }

    public OrgaoResponsavel getOrgaoResponsavel() {
        return orgaoResponsavel;
    }

    public void setOrgaoResponsavel(OrgaoResponsavel orgaoResponsavel) {
        this.orgaoResponsavel = orgaoResponsavel;
    }

    public TecnicoCampo getTecnico() {
        return tecnico;
    }

    public void setTecnico(TecnicoCampo tecnico) {
        this.tecnico = tecnico;
    }
}
