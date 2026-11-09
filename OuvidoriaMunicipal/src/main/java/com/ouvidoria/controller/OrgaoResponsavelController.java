package com.ouvidoria.controller;

import com.ouvidoria.entity.OrgaoResponsavel;
import com.ouvidoria.enums.Role;
import com.ouvidoria.security.RequireRole;
import com.ouvidoria.service.OrgaoResponsavelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orgaos")
@CrossOrigin(origins = "*")
public class OrgaoResponsavelController {

    @Autowired
    private OrgaoResponsavelService serv;

    @PostMapping
    @RequireRole({Role.ADMIN})
    public ResponseEntity<OrgaoResponsavel> cadastrar(@RequestBody OrgaoResponsavel o) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serv.salvar(o));
    }

    @GetMapping("/{id}")
    @RequireRole({Role.CIDADAO, Role.TECNICO, Role.ADMIN})
    public ResponseEntity<OrgaoResponsavel> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(serv.buscarPorId(id));
    }

    @GetMapping
    @RequireRole({Role.CIDADAO, Role.TECNICO, Role.ADMIN})
    public ResponseEntity<List<OrgaoResponsavel>> listar() {
        return ResponseEntity.ok(serv.listarTodos());
    }
}
