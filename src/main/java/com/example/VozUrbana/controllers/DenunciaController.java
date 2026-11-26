package com.example.VozUrbana.controllers;

import com.example.VozUrbana.dto.DenunciaCreateDTO;
import com.example.VozUrbana.dto.DenunciaResponseDTO;
import com.example.VozUrbana.dto.DenunciaUpdateDTO;
import com.example.VozUrbana.models.Imagem;
import com.example.VozUrbana.models.Notificacao;
import com.example.VozUrbana.projection.DenunciaSummary;
import com.example.VozUrbana.services.DenunciaService;
import com.example.VozUrbana.services.ImagemService;
import com.example.VozUrbana.services.NotificacaoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/denuncia")
public class DenunciaController {
    private final DenunciaService service;
    private final ImagemService imagemService;
    private final NotificacaoService notificacaoService;

    public DenunciaController(DenunciaService service, ImagemService imagemService,
                              NotificacaoService notificacaoService) {
        this.service = service;
        this.imagemService = imagemService;
        this.notificacaoService = notificacaoService;
    }

    @GetMapping
    public List<DenunciaSummary> findAll() { return service.findAll(); }

    @GetMapping("/user")
    public List<DenunciaSummary> findOwnSummary() { return service.findOwnSummary(); }

    @GetMapping("/{id}")
    public DenunciaResponseDTO findById(@PathVariable Integer id) { return service.getById(id); }

    @PostMapping
    public ResponseEntity<DenunciaResponseDTO> create(@Valid @RequestBody DenunciaCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public DenunciaResponseDTO update(@PathVariable Integer id, @Valid @RequestBody DenunciaUpdateDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/imagens")
    public List<Imagem> findImagens(@PathVariable Integer id) {
        return imagemService.findByDenuncia(id);
    }

    @GetMapping("/{id}/notificacoes")
    public List<Notificacao> findNotificacoes(@PathVariable Integer id) {
        return notificacaoService.findByDenuncia(id);
    }
}
