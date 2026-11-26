package com.example.VozUrbana.models;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "denuncia")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDenuncia;

    @ManyToOne
    @JoinColumn(name = "idRelator", nullable = false)
    private Relator relator;

    @ManyToOne
    @JoinColumn(name = "idAdministrador")
    private Administrador administrador;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, length = 20)
    private String status = "PENDENTE";
}