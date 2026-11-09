package com.ouvidoria.controller;

import com.ouvidoria.dto.LoginRequest;
import com.ouvidoria.dto.LoginResponse;
import com.ouvidoria.entity.Cidadao;
import com.ouvidoria.entity.Servidor;
import com.ouvidoria.entity.TecnicoCampo;
import com.ouvidoria.enums.Role;
import com.ouvidoria.repository.CidadaoRepository;
import com.ouvidoria.repository.ServidorRepository;
import com.ouvidoria.security.RequireRole;
import com.ouvidoria.security.SessaoUsuario;
import com.ouvidoria.security.SessionStore;
import com.ouvidoria.security.UsuarioAtual;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private CidadaoRepository cidadaoRepo;

    @Autowired
    private ServidorRepository servidorRepo;

    @Autowired
    private SessionStore sessionStore;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        if (req.email() == null || req.senha() == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Informe e-mail e senha"));
        }

        Optional<Cidadao> cidadao = cidadaoRepo.findByEmail(req.email());
        if (cidadao.isPresent() && cidadao.get().isAtivo() && cidadao.get().autenticar(req.senha())) {
            SessaoUsuario sessao = sessionStore.criar(
                    cidadao.get().getId(), cidadao.get().getNome(), cidadao.get().getEmail(), Role.CIDADAO);
            return ResponseEntity.ok(LoginResponse.de(sessao));
        }

        Optional<Servidor> servidor = servidorRepo.findByEmail(req.email());
        if (servidor.isPresent() && servidor.get().isAtivo() && servidor.get().autenticar(req.senha())) {
            Role papel = (servidor.get() instanceof TecnicoCampo) ? Role.TECNICO : Role.ADMIN;
            SessaoUsuario sessao = sessionStore.criar(
                    servidor.get().getId(), servidor.get().getNome(), servidor.get().getEmail(), papel);
            return ResponseEntity.ok(LoginResponse.de(sessao));
        }

        return ResponseEntity.status(401).body(Map.of("erro", "E-mail ou senha invalidos"));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            sessionStore.remover(header.substring(7).trim());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @RequireRole({Role.CIDADAO, Role.TECNICO, Role.ADMIN})
    public ResponseEntity<LoginResponse> me(HttpServletRequest request) {
        SessaoUsuario sessao = UsuarioAtual.de(request);
        return ResponseEntity.ok(LoginResponse.de(sessao));
    }
}
