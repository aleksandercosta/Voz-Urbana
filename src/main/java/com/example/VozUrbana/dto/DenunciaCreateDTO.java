package com.example.VozUrbana.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DenunciaCreateDTO(
        @NotNull Integer idRelator,
        @NotBlank @Size(max = 100) String titulo,
        @NotBlank String descricao
) {}
