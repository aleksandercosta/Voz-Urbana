package com.example.VozUrbana.dto;

import jakarta.validation.constraints.Size;

public record DenunciaUpdateDTO(
        @Size(min = 1, max = 100) String titulo,
        String descricao,
        @Size(min = 1, max = 20) String status
) {}
