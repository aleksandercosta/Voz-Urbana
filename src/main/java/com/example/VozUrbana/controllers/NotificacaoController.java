package com.example.VozUrbana.controllers;

import com.example.VozUrbana.models.Notificacao;
import com.example.VozUrbana.services.NotificacaoService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacao")
public class NotificacaoController {

    @Autowired
    private NotificacaoService notificacaoService;

    @GetMapping
    public List<Notificacao> findAll() {
        return notificacaoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notificacao> findById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                notificacaoService.findById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Notificacao> create(
            @Valid @RequestBody Notificacao notificacao) {

        return ResponseEntity.ok(
                notificacaoService.create(notificacao)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notificacao> update(
            @PathVariable Integer id,
            @Valid @RequestBody Notificacao notificacao) {

        return ResponseEntity.ok(
                notificacaoService.update(id, notificacao)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id) {

        notificacaoService.delete(id);

        return ResponseEntity.noContent().build();
    }
}