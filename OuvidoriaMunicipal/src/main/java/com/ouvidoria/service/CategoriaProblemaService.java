package com.ouvidoria.service;

import com.ouvidoria.entity.CategoriaProblema;
import com.ouvidoria.repository.CategoriaProblemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaProblemaService {

    @Autowired
    private CategoriaProblemaRepository dao;

    public CategoriaProblema salvar(CategoriaProblema c) {
        return dao.save(c);
    }

    public CategoriaProblema buscarPorId(Long id) {
        return dao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria nao encontrada: id " + id));
    }

    public List<CategoriaProblema> listarTodos() {
        return dao.findAll();
    }

    public void desativar(Long id) {
        CategoriaProblema c = buscarPorId(id);
        c.desativar();
        dao.save(c);
    }
}
