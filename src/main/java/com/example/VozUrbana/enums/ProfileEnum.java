package com.example.VozUrbana.enums;

import lombok.Getter;

@Getter
public enum ProfileEnum {

    USER(1, "ROLE_USER"),
    ADMIN(2, "ROLE_ADMIN");

    private final Integer codigo;
    private final String descricao;

    ProfileEnum(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static ProfileEnum toEnum(Integer codigo) {
        if (codigo == null) {
            return null;
        }

        for (ProfileEnum profile : ProfileEnum.values()) {
            if (profile.getCodigo().equals(codigo)) {
                return profile;
            }
        }

        throw new IllegalArgumentException("Código de perfil inválido: " + codigo);
    }
}
