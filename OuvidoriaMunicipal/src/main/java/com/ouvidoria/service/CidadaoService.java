package com.ouvidoria.service;

import com.ouvidoria.entity.Cidadao;
import com.ouvidoria.repository.CidadaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CidadaoService {

    @Autowired
    private CidadaoRepository dao;

    public Cidadao salvar(Cidadao c) {
        if (!validarDados(c)) {
            throw new IllegalArgumentException("Dados invalidos para cadastro de cidadao");
        }
        if (c.getId() == null && cpfDuplicado(c.getCpf())) {
            throw new IllegalArgumentException("Ja existe um cidadao cadastrado com esse CPF");
        }
        return dao.save(c);
    }

    public boolean validarDados(Cidadao c) {
        return c != null
                && c.getNome() != null && !c.getNome().isBlank()
                && c.validarCpf();
    }

    public boolean cpfDuplicado(String cpf) {
        return dao.findByCpf(cpf).isPresent();
    }

    public Cidadao buscarPorId(Long id) {
        return dao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cidadao nao encontrado: id " + id));
    }

    public List<Cidadao> listarTodos() {
        return dao.findAll();
    }

    public void inativar(Long id) {
        Cidadao c = buscarPorId(id);
        c.inativar("inativado via API");
        dao.save(c);
    }
}
