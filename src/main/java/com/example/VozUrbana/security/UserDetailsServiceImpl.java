package com.example.VozUrbana.security;

import com.example.VozUrbana.models.Usuario;
import com.example.VozUrbana.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado com e-mail: " + username
                ));

        return new UsuarioSpringSecurity(
                usuario.getIdUsuario(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getProfiles()
        );
    }
}
