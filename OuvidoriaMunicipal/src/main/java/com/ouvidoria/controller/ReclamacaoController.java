package com.ouvidoria.controller;

import com.ouvidoria.entity.Cidadao;
import com.ouvidoria.entity.Reclamacao;
import com.ouvidoria.enums.Role;
import com.ouvidoria.enums.StatusReclamacao;
import com.ouvidoria.security.RequireRole;
import com.ouvidoria.security.SessaoUsuario;
import com.ouvidoria.security.UsuarioAtual;
import com.ouvidoria.service.ReclamacaoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reclamacoes")
@CrossOrigin(origins = "*")
public class ReclamacaoController {

    @Autowired
    private ReclamacaoService serv;

    @PostMapping
    @RequireRole({Role.CIDADAO, Role.TECNICO, Role.ADMIN})
    public ResponseEntity<Reclamacao> abrir(@RequestBody Reclamacao r, HttpServletRequest request) {
        SessaoUsuario usuario = UsuarioAtual.de(request);
        if (usuario.role() == Role.CIDADAO) {
            // um cidadao so pode abrir reclamacao em nome dele mesmo,
            // mesmo que o corpo da requisicao tente informar outro id
            Cidadao c = new Cidadao();
            c.setId(usuario.pessoaId());
            r.setCidadao(c);
        }
        Reclamacao aberta = serv.abrir(r);
        return ResponseEntity.status(HttpStatus.CREATED).body(aberta);
    }

    @GetMapping("/{id}")
    @RequireRole({Role.CIDADAO, Role.TECNICO, Role.ADMIN})
    public ResponseEntity<Reclamacao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(serv.buscarPorId(id));
    }

    @GetMapping("/protocolo/{protocolo}")
    @RequireRole({Role.CIDADAO, Role.TECNICO, Role.ADMIN})
    public ResponseEntity<Reclamacao> buscarPorProtocolo(@PathVariable String protocolo) {
        return ResponseEntity.ok(serv.buscarPorProtocolo(protocolo));
    }

    @GetMapping
    @RequireRole({Role.CIDADAO, Role.TECNICO, Role.ADMIN})
    public ResponseEntity<List<Reclamacao>> listar() {
        return ResponseEntity.ok(serv.listarTodos());
    }

    @GetMapping("/cidadao/{idCidadao}")
    @RequireRole({Role.CIDADAO, Role.TECNICO, Role.ADMIN})
    public ResponseEntity<List<Reclamacao>> listarMinhasReclamacoes(@PathVariable Long idCidadao) {
        return ResponseEntity.ok(serv.listarMinhasReclamacoes(idCidadao));
    }

    @GetMapping("/bairro/{bairro}")
    @RequireRole({Role.CIDADAO, Role.TECNICO, Role.ADMIN})
    public ResponseEntity<List<Reclamacao>> listarPorBairro(@PathVariable String bairro) {
        return ResponseEntity.ok(serv.listarPorBairro(bairro));
    }

    @PatchMapping("/{id}/status")
    @RequireRole({Role.ADMIN})
    public ResponseEntity<Void> alterarStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        StatusReclamacao novo = StatusReclamacao.valueOf(body.get("status"));
        serv.alterarStatus(id, novo);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancelar")
    @RequireRole({Role.ADMIN})
    public ResponseEntity<Void> cancelar(@PathVariable Long id, @RequestBody Map<String, String> body) {
        serv.cancelar(id, body.get("motivo"));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reabrir")
    @RequireRole({Role.ADMIN})
    public ResponseEntity<Void> reabrir(@PathVariable Long id, @RequestBody Map<String, String> body) {
        serv.reabrir(id, body.get("motivo"));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/avaliar")
    @RequireRole({Role.CIDADAO, Role.ADMIN})
    public ResponseEntity<Void> avaliar(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        serv.avaliar(id, body.get("nota"));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/atrasadas")
    @RequireRole({Role.ADMIN})
    public ResponseEntity<List<Reclamacao>> escalonarAtrasadas() {
        return ResponseEntity.ok(serv.escalonarAtrasadas());
    }
}
