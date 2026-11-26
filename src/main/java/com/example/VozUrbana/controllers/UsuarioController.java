package com.example.VozUrbana.controllers;

import com.example.VozUrbana.dto.UsuarioCreateDTO;
import com.example.VozUrbana.dto.UsuarioResponseDTO;
import com.example.VozUrbana.dto.UsuarioUpdateDTO;
import com.example.VozUrbana.models.Notificacao;
import com.example.VozUrbana.projection.UsuarioSummary;
import com.example.VozUrbana.services.NotificacaoService;
import com.example.VozUrbana.services.UsuarioService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final NotificacaoService notificacaoService;

    public UsuarioController(UsuarioService usuarioService, NotificacaoService notificacaoService) {
        this.usuarioService = usuarioService;
        this.notificacaoService = notificacaoService;
    }

    @GetMapping
    public List<UsuarioSummary> findAll() { return usuarioService.findAll(); }

    @GetMapping("/user")
    public List<UsuarioSummary> findOwnSummary() { return usuarioService.findOwnSummary(); }

    @GetMapping("/me")
    public UsuarioResponseDTO currentUser() { return usuarioService.currentUser(); }

    @GetMapping("/{id}")
    public UsuarioResponseDTO findById(@PathVariable Integer id) {
        return usuarioService.findById(id);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> create(@Valid @RequestBody UsuarioCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.create(dto));
    }

    @PutMapping("/{id}")
    public UsuarioResponseDTO update(@PathVariable Integer id, @Valid @RequestBody UsuarioUpdateDTO dto) {
        return usuarioService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/notificacoes")
    public List<Notificacao> findNotificacoes(@PathVariable Integer id) {
        usuarioService.checkAccess(id);
        return notificacaoService.findByUsuario(id);
    }
}
