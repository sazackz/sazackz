package com.ouvidoria.security;

import com.ouvidoria.enums.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * Marca um endpoint (ou controller inteiro) como protegido: so pode ser
 * acessado por quem estiver logado E tiver um dos papeis listados.
 * Endpoint sem essa anotacao e publico (ex.: login, cadastro de cidadao).
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    Role[] value();
}
