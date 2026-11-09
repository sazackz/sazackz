package com.ouvidoria.controller;

import com.ouvidoria.entity.TecnicoCampo;
import com.ouvidoria.enums.Role;
import com.ouvidoria.security.RequireRole;
import com.ouvidoria.service.TecnicoCampoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tecnicos")
@CrossOrigin(origins = "*")
@RequireRole({Role.TECNICO, Role.ADMIN})
public class TecnicoCampoController {

    @Autowired
    private TecnicoCampoService serv;

    @PostMapping
    @RequireRole({Role.ADMIN})
    public ResponseEntity<TecnicoCampo> cadastrar(@RequestBody TecnicoCampo t) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serv.salvar(t));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TecnicoCampo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(serv.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<TecnicoCampo>> listar() {
        return ResponseEntity.ok(serv.listarTodos());
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<TecnicoCampo>> listarDisponiveis() {
        return ResponseEntity.ok(serv.listarDisponiveis());
    }
}
