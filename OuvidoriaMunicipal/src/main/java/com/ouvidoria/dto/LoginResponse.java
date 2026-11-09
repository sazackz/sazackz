package com.ouvidoria.dto;

import com.ouvidoria.enums.Role;
import com.ouvidoria.security.SessaoUsuario;

public record LoginResponse(String token, Long id, String nome, String email, Role role) {
    public static LoginResponse de(SessaoUsuario sessao) {
        return new LoginResponse(sessao.token(), sessao.pessoaId(), sessao.nome(), sessao.email(), sessao.role());
    }
}
