package com.example.VozUrbana.models;

import java.time.LocalDate;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "imagem")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Imagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idImagem;

    @ManyToOne
    @JoinColumn(name = "idDenuncia", nullable = false)
    private Denuncia denuncia;

    @Column(nullable = false, length = 100)
    private String nomeArquivo;

    @Column(nullable = false, length = 255)
    private String caminho;

    @Column(nullable = false, length = 10)
    private String formato;

    @Column(nullable = false)
    private LocalDate dataUpload;
}