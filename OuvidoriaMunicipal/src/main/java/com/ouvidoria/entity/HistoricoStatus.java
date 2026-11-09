package com.ouvidoria.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ouvidoria.enums.StatusReclamacao;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "historico_status")
public class HistoricoStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private StatusReclamacao statusAnterior;

    @Enumerated(EnumType.STRING)
    private StatusReclamacao statusNovo;

    private LocalDateTime dataHora;
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reclamacao")
    @JsonIgnore
    private Reclamacao reclamacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servidor")
    private Servidor servidor;

    public HistoricoStatus() {
    }

    public void registrar(Reclamacao r, Servidor s) {
        this.reclamacao = r;
        this.servidor = s;
        this.dataHora = LocalDateTime.now();
    }

    public Duration calcularTempoNoStatus() {
        if (dataHora == null) {
            return Duration.ZERO;
        }
        return Duration.between(dataHora, LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusReclamacao getStatusAnterior() {
        return statusAnterior;
    }

    public void setStatusAnterior(StatusReclamacao statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    public StatusReclamacao getStatusNovo() {
        return statusNovo;
    }

    public void setStatusNovo(StatusReclamacao statusNovo) {
        this.statusNovo = statusNovo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Reclamacao getReclamacao() {
        return reclamacao;
    }

    public void setReclamacao(Reclamacao reclamacao) {
        this.reclamacao = reclamacao;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }
}
