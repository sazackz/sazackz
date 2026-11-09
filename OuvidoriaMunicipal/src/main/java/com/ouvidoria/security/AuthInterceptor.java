package com.ouvidoria.security;

import com.ouvidoria.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private SessionStore sessionStore;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        RequireRole required = handlerMethod.getMethodAnnotation(RequireRole.class);
        if (required == null) {
            required = handlerMethod.getBeanType().getAnnotation(RequireRole.class);
        }
        if (required == null) {
            return true; // endpoint publico
        }

        String token = extrairToken(request);
        if (token == null) {
            responder(response, 401, "Nao autenticado. Faca login.");
            return false;
        }

        Optional<SessaoUsuario> sessao = sessionStore.buscar(token);
        if (sessao.isEmpty()) {
            responder(response, 401, "Sessao invalida ou expirada. Faca login novamente.");
            return false;
        }

        Role papel = sessao.get().role();
        boolean permitido = Arrays.asList(required.value()).contains(papel);
        if (!permitido) {
            responder(response, 403, "Seu perfil (" + papel + ") nao tem acesso a este recurso.");
            return false;
        }

        request.setAttribute("usuarioLogado", sessao.get());
        return true;
    }

    private String extrairToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7).trim();
        }
        return null;
    }

    private void responder(HttpServletResponse response, int status, String mensagem) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"erro\":\"" + mensagem.replace("\"", "'") + "\"}");
    }
}
