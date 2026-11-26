package com.example.VozUrbana.controllers;

import com.example.VozUrbana.models.Imagem;
import com.example.VozUrbana.services.ImagemService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/imagem")
public class ImagemController {

    @Autowired
    private ImagemService imagemService;

    @GetMapping
    public List<Imagem> findAll() {
        return imagemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Imagem> findById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                imagemService.findById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Imagem> create(
            @Valid @RequestBody Imagem imagem) {

        return ResponseEntity.ok(
                imagemService.create(imagem)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Imagem> update(
            @PathVariable Integer id,
            @Valid @RequestBody Imagem imagem) {

        return ResponseEntity.ok(
                imagemService.update(id, imagem)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id) {

        imagemService.delete(id);

        return ResponseEntity.noContent().build();
    }
}