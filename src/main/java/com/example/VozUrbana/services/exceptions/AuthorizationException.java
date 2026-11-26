package com.example.VozUrbana.services.exceptions;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String mensagem) {
        super(mensagem);
    }
}
