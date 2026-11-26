package com.example.VozUrbana.services;

import java.util.List;
import com.example.VozUrbana.dto.DenunciaCreateDTO;
import com.example.VozUrbana.dto.DenunciaResponseDTO;
import com.example.VozUrbana.dto.DenunciaUpdateDTO;
import com.example.VozUrbana.models.Denuncia;
import com.example.VozUrbana.models.Relator;
import com.example.VozUrbana.projection.DenunciaSummary;
import com.example.VozUrbana.repositories.DenunciaRepository;
import com.example.VozUrbana.services.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DenunciaService {
    private final DenunciaRepository repository;
    private final RelatorService relatorService;
    private final AcessoService acesso;

    public DenunciaService(DenunciaRepository repository, RelatorService relatorService, AcessoService acesso) {
        this.repository = repository;
        this.relatorService = relatorService;
        this.acesso = acesso;
    }

    @Transactional(readOnly = true)
    public List<DenunciaSummary> findAll() {
        if (acesso.isAdmin()) return repository.findAllProjectedBy();
        return findOwnSummary();
    }

    @Transactional(readOnly = true)
    public List<DenunciaSummary> findOwnSummary() {
        return repository.findSummaryByRelator_Usuario_IdUsuario(acesso.authenticated().getId());
    }

    public Denuncia findById(Integer id) {
        Denuncia denuncia = repository.findById(id).orElseThrow(() ->
            new ObjectNotFoundException("Denúncia não encontrada com ID: " + id));
        acesso.checkOwnerOrAdmin(denuncia.getRelator().getUsuario().getIdUsuario());
        return denuncia;
    }

    public DenunciaResponseDTO getById(Integer id) {
        return DenunciaResponseDTO.from(findById(id));
    }

    public DenunciaResponseDTO create(DenunciaCreateDTO dto) {
        Relator relator = relatorService.findById(dto.idRelator());
        Denuncia denuncia = new Denuncia();
        denuncia.setRelator(relator);
        denuncia.setTitulo(dto.titulo().trim());
        denuncia.setDescricao(dto.descricao().trim());
        denuncia.setStatus("PENDENTE");
        return DenunciaResponseDTO.from(repository.save(denuncia));
    }

    public DenunciaResponseDTO update(Integer id, DenunciaUpdateDTO dto) {
        Denuncia atual = findById(id);
        if (dto.titulo() != null) atual.setTitulo(dto.titulo().trim());
        if (dto.descricao() != null) atual.setDescricao(dto.descricao());
        if (dto.status() != null) {
            acesso.requireAdmin(); // Usuário comum não altera status.
            atual.setStatus(dto.status());
        }
        return DenunciaResponseDTO.from(repository.save(atual));
    }

    public void delete(Integer id) { repository.delete(findById(id)); }

    public List<Denuncia> findByRelator(Integer idRelator) {
        relatorService.findById(idRelator);
        return repository.findByRelator_IdRelator(idRelator);
    }

    public List<Denuncia> findByAdministrador(Integer idAdministrador) {
        acesso.requireAdmin();
        return repository.findByAdministrador_IdAdministrador(idAdministrador);
    }
}
