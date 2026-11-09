package com.ouvidoria.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cidadao")
public class Cidadao extends Pessoa {

    private LocalDateTime dataCadastro;
    private boolean ativo = true;
    private int pontuacaoConfiabilidade = 100;

    @OneToMany(mappedBy = "cidadao")
    @JsonIgnore
    private List<Reclamacao> reclamacoes = new ArrayList<>();

    public Cidadao() {
        super();
        this.dataCadastro = LocalDateTime.now();
    }

    public void abrirReclamacao(Reclamacao r) {
        r.setCidadao(this);
        r.abrir();
        this.reclamacoes.add(r);
    }

    public List<Reclamacao> listarMinhasReclamacoes() {
        return reclamacoes;
    }

    public void avaliarAtendimento(Reclamacao r, int nota) {
        r.avaliar(nota);
    }

    public void inativar(String motivo) {
        this.ativo = false;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public int getPontuacaoConfiabilidade() {
        return pontuacaoConfiabilidade;
    }

    public void setPontuacaoConfiabilidade(int pontuacaoConfiabilidade) {
        this.pontuacaoConfiabilidade = pontuacaoConfiabilidade;
    }
}
