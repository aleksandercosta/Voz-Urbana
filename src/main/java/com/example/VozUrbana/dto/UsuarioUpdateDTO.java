package com.example.VozUrbana.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateDTO(
        @Size(min = 1, max = 100) String nome,
        @Email @Size(max = 100) String email,
        @Size(min = 6, max = 255) String senha,
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 números") String cpf
) {}
