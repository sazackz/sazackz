package com.ouvidoria.controller;

import com.ouvidoria.entity.ObservacaoOrdem;
import com.ouvidoria.entity.OrdemServico;
import com.ouvidoria.enums.Role;
import com.ouvidoria.security.RequireRole;
import com.ouvidoria.security.SessaoUsuario;
import com.ouvidoria.security.UsuarioAtual;
import jakarta.servlet.http.HttpServletRequest;
import com.ouvidoria.service.OrdemServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ordens-servico")
@CrossOrigin(origins = "*")
@RequireRole({Role.TECNICO, Role.ADMIN})
public class OrdemServicoController {

    @Autowired
    private OrdemServicoService serv;

    @PostMapping
    public ResponseEntity<OrdemServico> abrir(@RequestBody Map<String, Long> body) {
        OrdemServico os = serv.abrirParaReclamacao(body.get("idReclamacao"), body.get("idOrgao"));
        return ResponseEntity.status(HttpStatus.CREATED).body(os);
    }

    @PatchMapping("/{id}/tecnico")
    public ResponseEntity<OrdemServico> atribuirTecnico(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        return ResponseEntity.ok(serv.atribuirTecnico(id, body.get("idTecnico")));
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<OrdemServico> concluir(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String relatorio = (String) body.get("relatorio");
        double custoReal = body.get("custoReal") != null ? ((Number) body.get("custoReal")).doubleValue() : 0;
        return ResponseEntity.ok(serv.concluir(id, relatorio, custoReal));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemServico> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(serv.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<OrdemServico>> listar() {
        return ResponseEntity.ok(serv.listarTodos());
    }

    @GetMapping("/{id}/observacoes")
    public ResponseEntity<List<ObservacaoOrdem>> listarObservacoes(@PathVariable Long id) {
        return ResponseEntity.ok(serv.listarObservacoes(id));
    }

    @PostMapping("/{id}/observacoes")
    public ResponseEntity<ObservacaoOrdem> adicionarObservacao(@PathVariable Long id,
                                                               @RequestBody Map<String, String> body,
                                                               HttpServletRequest request) {
        SessaoUsuario usuario = UsuarioAtual.de(request);
        ObservacaoOrdem obs = serv.adicionarObservacao(id, body.get("texto"), usuario.nome(), usuario.role().name());
        return ResponseEntity.status(HttpStatus.CREATED).body(obs);
    }
}
