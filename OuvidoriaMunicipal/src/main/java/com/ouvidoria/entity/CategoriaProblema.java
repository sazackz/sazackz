package com.ouvidoria.entity;

import com.ouvidoria.enums.Prioridade;
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

import java.time.LocalDateTime;

@Entity
@Table(name = "categoria_problema")
public class CategoriaProblema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private int prazoSlaHoras;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridadePadrao;

    private boolean ativa = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orgao_padrao")
    private OrgaoResponsavel orgaoPadrao;

    public CategoriaProblema() {
    }

    public LocalDateTime calcularPrazo(Prioridade p) {
        int horas = this.prazoSlaHoras;
        if (p == Prioridade.URGENTE) {
            horas = horas / 2;
        } else if (p == Prioridade.ALTA) {
            horas = (int) (horas * 0.75);
        }
        return LocalDateTime.now().plusHours(horas);
    }

    public void ativar() {
        this.ativa = true;
    }

    public void desativar() {
        this.ativa = false;
    }

    public void definirOrgaoPadrao(OrgaoResponsavel o) {
        this.orgaoPadrao = o;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getPrazoSlaHoras() {
        return prazoSlaHoras;
    }

    public void setPrazoSlaHoras(int prazoSlaHoras) {
        this.prazoSlaHoras = prazoSlaHoras;
    }

    public Prioridade getPrioridadePadrao() {
        return prioridadePadrao;
    }

    public void setPrioridadePadrao(Prioridade prioridadePadrao) {
        this.prioridadePadrao = prioridadePadrao;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public OrgaoResponsavel getOrgaoPadrao() {
        return orgaoPadrao;
    }

    public void setOrgaoPadrao(OrgaoResponsavel orgaoPadrao) {
        this.orgaoPadrao = orgaoPadrao;
    }
}
