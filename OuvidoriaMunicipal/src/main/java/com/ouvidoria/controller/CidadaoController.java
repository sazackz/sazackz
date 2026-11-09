package com.ouvidoria.controller;

import com.ouvidoria.entity.Cidadao;
import com.ouvidoria.enums.Role;
import com.ouvidoria.security.RequireRole;
import com.ouvidoria.service.CidadaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cidadaos")
@CrossOrigin(origins = "*")
public class CidadaoController {

    @Autowired
    private CidadaoService serv;

    // publico: e o auto-cadastro ("Criar conta de cidadao")
    @PostMapping
    public ResponseEntity<Cidadao> cadastrar(@RequestBody Cidadao c) {
        Cidadao salvo = serv.salvar(c);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    @RequireRole({Role.ADMIN})
    public ResponseEntity<Cidadao> atualizar(@PathVariable Long id, @RequestBody Cidadao c) {
        c.setId(id);
        return ResponseEntity.ok(serv.salvar(c));
    }

    @GetMapping("/{id}")
    @RequireRole({Role.CIDADAO, Role.TECNICO, Role.ADMIN})
    public ResponseEntity<Cidadao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(serv.buscarPorId(id));
    }

    @GetMapping
    @RequireRole({Role.ADMIN})
    public ResponseEntity<List<Cidadao>> listar() {
        return ResponseEntity.ok(serv.listarTodos());
    }

    @DeleteMapping("/{id}")
    @RequireRole({Role.ADMIN})
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        serv.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
