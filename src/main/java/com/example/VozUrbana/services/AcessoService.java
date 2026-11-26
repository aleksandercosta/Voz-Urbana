package com.example.VozUrbana.services;

import com.example.VozUrbana.enums.ProfileEnum;
import com.example.VozUrbana.security.UsuarioSpringSecurity;
import com.example.VozUrbana.services.exceptions.AuthorizationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AcessoService {

    public UsuarioSpringSecurity authenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UsuarioSpringSecurity usuario)) {
            throw new AuthorizationException("Acesso negado!");
        }
        return usuario;
    }

    public boolean isAdmin() {
        return authenticated().hasRole(ProfileEnum.ADMIN);
    }

    public void requireAdmin() {
        if (!isAdmin()) {
            throw new AuthorizationException("Acesso negado!");
        }
    }

    public void checkOwnerOrAdmin(Integer idDono) {
        UsuarioSpringSecurity atual = authenticated();
        if (!atual.hasRole(ProfileEnum.ADMIN) && !atual.getId().equals(idDono)) {
            throw new AuthorizationException("Acesso negado!");
        }
    }
}
