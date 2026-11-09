package com.ouvidoria.service;

import com.ouvidoria.entity.OrgaoResponsavel;
import com.ouvidoria.repository.OrgaoResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrgaoResponsavelService {

    @Autowired
    private OrgaoResponsavelRepository dao;

    public OrgaoResponsavel salvar(OrgaoResponsavel o) {
        return dao.save(o);
    }

    public OrgaoResponsavel buscarPorId(Long id) {
        return dao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orgao nao encontrado: id " + id));
    }

    public List<OrgaoResponsavel> listarTodos() {
        return dao.findAll();
    }
}
