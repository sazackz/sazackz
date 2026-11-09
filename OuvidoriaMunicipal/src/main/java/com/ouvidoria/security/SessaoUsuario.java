package com.ouvidoria.security;

import com.ouvidoria.enums.Role;

import java.time.Instant;

/*
 * Representa uma sessao de login ativa (token simples em memoria,
 * sem necessidade de JWT ou Spring Security completo para o escopo do projeto).
 */
public record SessaoUsuario(
        String token,
        Long pessoaId,
        String nome,
        String email,
        Role role,
        Instant criadoEm
) {
}
