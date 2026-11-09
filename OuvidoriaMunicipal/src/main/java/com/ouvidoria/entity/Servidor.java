package com.ouvidoria.entity;

import com.ouvidoria.enums.Prioridade;
import com.ouvidoria.enums.StatusReclamacao;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "servidor")
public class Servidor extends Pessoa {

    private String matricula;
    private String cargo;
    private LocalDate dataAdmissao;
    private boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orgao_responsavel")
    private OrgaoResponsavel orgaoResponsavel;

    public Servidor() {
        super();
    }

    public void triarReclamacao(Reclamacao r) {
        if (r != null) {
            r.setStatus(StatusReclamacao.EM_TRIAGEM);
        }
    }

    public void encaminhar(Reclamacao r, OrgaoResponsavel o) {
        if (r != null) {
            r.setStatus(StatusReclamacao.ENCAMINHADA);
        }
    }

    public void alterarStatus(Reclamacao r, StatusReclamacao s) {
        if (r != null) {
            r.setStatus(s);
        }
    }

    public void definirPrioridade(Reclamacao r, Prioridade p) {
        if (r != null) {
            r.setPrioridade(p);
        }
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public OrgaoResponsavel getOrgaoResponsavel() {
        return orgaoResponsavel;
    }

    public void setOrgaoResponsavel(OrgaoResponsavel orgaoResponsavel) {
        this.orgaoResponsavel = orgaoResponsavel;
    }
}
