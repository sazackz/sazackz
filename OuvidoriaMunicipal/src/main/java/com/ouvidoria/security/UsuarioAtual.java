package com.ouvidoria.security;

import jakarta.servlet.http.HttpServletRequest;

/*
 * Helper para os controllers lerem quem esta logado na requisicao atual.
 * O AuthInterceptor e quem preenche esse atributo, apos validar o token.
 */
public final class UsuarioAtual {

    private UsuarioAtual() {
    }

    public static SessaoUsuario de(HttpServletRequest request) {
        return (SessaoUsuario) request.getAttribute("usuarioLogado");
    }
}
