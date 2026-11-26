package com.example.VozUrbana.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioCreateDTO(
        @NotBlank @Size(max = 100) String nome,
        @NotBlank @Email @Size(max = 100) String email,
        @NotBlank @Size(min = 6, max = 255) String senha,
        @NotBlank @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 números") String cpf
) {}
