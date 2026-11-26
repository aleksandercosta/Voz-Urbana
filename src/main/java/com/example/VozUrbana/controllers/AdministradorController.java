package com.example.VozUrbana.controllers;

import com.example.VozUrbana.models.Administrador;
import com.example.VozUrbana.models.Denuncia;
import com.example.VozUrbana.services.AdministradorService;
import com.example.VozUrbana.services.DenunciaService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrador")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @Autowired
    private DenunciaService denunciaService;

    @GetMapping
    public List<Administrador> findAll() {
        return administradorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Administrador> findById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                administradorService.findById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Administrador> create(
            @Valid @RequestBody Administrador administrador) {

        return ResponseEntity.ok(
                administradorService.create(administrador)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Administrador> update(
            @PathVariable Integer id,
            @Valid @RequestBody Administrador administrador) {

        return ResponseEntity.ok(
                administradorService.update(id, administrador)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id) {

        administradorService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/denuncias")
    public ResponseEntity<List<Denuncia>> findDenuncias(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                denunciaService.findByAdministrador(id)
        );
    }
}