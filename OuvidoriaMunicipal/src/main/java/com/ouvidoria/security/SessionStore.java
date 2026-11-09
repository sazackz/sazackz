package com.ouvidoria.security;

import com.ouvidoria.enums.Role;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionStore {

    private static final long DURACAO_HORAS = 8;

    private final Map<String, SessaoUsuario> sessoes = new ConcurrentHashMap<>();

    public SessaoUsuario criar(Long pessoaId, String nome, String email, Role role) {
        String token = UUID.randomUUID().toString();
        SessaoUsuario sessao = new SessaoUsuario(token, pessoaId, nome, email, role, Instant.now());
        sessoes.put(token, sessao);
        return sessao;
    }

    public Optional<SessaoUsuario> buscar(String token) {
        SessaoUsuario sessao = sessoes.get(token);
        if (sessao == null) {
            return Optional.empty();
        }
        if (Instant.now().isAfter(sessao.criadoEm().plus(DURACAO_HORAS, ChronoUnit.HOURS))) {
            sessoes.remove(token);
            return Optional.empty();
        }
        return Optional.of(sessao);
    }

    public void remover(String token) {
        sessoes.remove(token);
    }
}
