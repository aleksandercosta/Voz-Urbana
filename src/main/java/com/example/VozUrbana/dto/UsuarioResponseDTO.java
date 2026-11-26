package com.example.VozUrbana.dto;

import java.time.LocalDate;
import com.example.VozUrbana.models.Usuario;

public record UsuarioResponseDTO(
        Integer idUsuario,
        String nome,
        String email,
        LocalDate dataCadastro
) {
    public static UsuarioResponseDTO from(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getIdUsuario(), usuario.getNome(),
                usuario.getEmail(), usuario.getDataCadastro());
    }
}
