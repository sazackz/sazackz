package com.ouvidoria.service;

import com.ouvidoria.entity.TecnicoCampo;
import com.ouvidoria.repository.TecnicoCampoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TecnicoCampoService {

    @Autowired
    private TecnicoCampoRepository dao;

    public TecnicoCampo salvar(TecnicoCampo t) {
        return dao.save(t);
    }

    public TecnicoCampo buscarPorId(Long id) {
        return dao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tecnico nao encontrado: id " + id));
    }

    public List<TecnicoCampo> listarTodos() {
        return dao.findAll();
    }

    public List<TecnicoCampo> listarDisponiveis() {
        return dao.findByDisponivelTrue();
    }
}
