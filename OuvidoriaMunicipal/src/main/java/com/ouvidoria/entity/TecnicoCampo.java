package com.ouvidoria.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "tecnico_campo")
public class TecnicoCampo extends Servidor {

    private String especialidade;
    private String veiculo;
    private boolean disponivel = true;

    public TecnicoCampo() {
        super();
    }

    public void iniciarServico(OrdemServico os) {
        if (os != null) {
            this.disponivel = false;
            os.atribuirTecnico(this);
        }
    }

    public void concluirServico(OrdemServico os, String relatorio) {
        if (os != null) {
            os.concluir(relatorio);
            this.disponivel = true;
        }
    }

    public void anexarFoto(OrdemServico os, Anexo a) {
        if (os != null && a != null) {
            a.setDataEnvio(LocalDateTime.now());
        }
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}
