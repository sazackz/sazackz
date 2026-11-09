package com.ouvidoria.controller;

import com.ouvidoria.entity.CategoriaProblema;
import com.ouvidoria.enums.Role;
import com.ouvidoria.security.RequireRole;
import com.ouvidoria.service.CategoriaProblemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaProblemaController {

    @Autowired
    private CategoriaProblemaService serv;

    @PostMapping
    @RequireRole({Role.ADMIN})
    public ResponseEntity<CategoriaProblema> cadastrar(@RequestBody CategoriaProblema c) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serv.salvar(c));
    }

    @GetMapping("/{id}")
    @RequireRole({Role.CIDADAO, Role.TECNICO, Role.ADMIN})
    public ResponseEntity<CategoriaProblema> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(serv.buscarPorId(id));
    }

    @GetMapping
    @RequireRole({Role.CIDADAO, Role.TECNICO, Role.ADMIN})
    public ResponseEntity<List<CategoriaProblema>> listar() {
        return ResponseEntity.ok(serv.listarTodos());
    }

    @DeleteMapping("/{id}")
    @RequireRole({Role.ADMIN})
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        serv.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
