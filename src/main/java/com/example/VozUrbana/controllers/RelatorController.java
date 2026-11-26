package com.example.VozUrbana.controllers;

import com.example.VozUrbana.models.Denuncia;
import com.example.VozUrbana.models.Relator;
import com.example.VozUrbana.services.DenunciaService;
import com.example.VozUrbana.services.RelatorService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relator")
public class RelatorController {

    @Autowired
    private RelatorService relatorService;

    @Autowired
    private DenunciaService denunciaService;

    @GetMapping
    public List<Relator> findAll() {
        return relatorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Relator> findById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                relatorService.findById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Relator> create(
            @Valid @RequestBody Relator relator) {

        return ResponseEntity.ok(
                relatorService.create(relator)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Relator> update(
            @PathVariable Integer id,
            @Valid @RequestBody Relator relator) {

        return ResponseEntity.ok(
                relatorService.update(id, relator)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id) {

        relatorService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/denuncias")
    public ResponseEntity<List<Denuncia>> findDenuncias(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                denunciaService.findByRelator(id)
        );
    }
}