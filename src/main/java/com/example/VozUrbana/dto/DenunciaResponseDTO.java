package com.example.VozUrbana.dto;

import com.example.VozUrbana.models.Denuncia;

public record DenunciaResponseDTO(
        Integer idDenuncia,
        Integer idRelator,
        String titulo,
        String descricao,
        String status
) {
    public static DenunciaResponseDTO from(Denuncia denuncia) {
        return new DenunciaResponseDTO(
                denuncia.getIdDenuncia(), denuncia.getRelator().getIdRelator(),
                denuncia.getTitulo(), denuncia.getDescricao(), denuncia.getStatus());
    }
}
