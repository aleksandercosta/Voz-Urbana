package com.example.VozUrbana.projection;

import java.time.LocalDate;

public interface UsuarioSummary {
    Integer getIdUsuario();
    String getNome();
    String getEmail();
    LocalDate getDataCadastro();
}
