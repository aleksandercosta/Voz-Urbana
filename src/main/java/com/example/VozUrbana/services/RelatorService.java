package com.example.VozUrbana.services;

import java.util.List;
import com.example.VozUrbana.models.Relator;
import com.example.VozUrbana.repositories.RelatorRepository;
import com.example.VozUrbana.repositories.UsuarioRepository;
import com.example.VozUrbana.services.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RelatorService {
    private final RelatorRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final AcessoService acesso;

    public RelatorService(RelatorRepository repository, UsuarioRepository usuarioRepository, AcessoService acesso) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.acesso = acesso;
    }

    public List<Relator> findAll() {
        return acesso.isAdmin() ? repository.findAll()
                : repository.findByUsuario_IdUsuario(acesso.authenticated().getId());
    }

    public Relator findById(Integer id) {
        Relator relator = repository.findById(id).orElseThrow(() ->
            new ObjectNotFoundException("Relator não encontrado com ID: " + id));
        acesso.checkOwnerOrAdmin(relator.getUsuario().getIdUsuario());
        return relator;
    }

    public Relator create(Relator relator) {
        relator.setIdRelator(null);
        // Nunca aceitar uma identidade de outro usuário fornecida no JSON.
        relator.setUsuario(usuarioRepository.getReferenceById(acesso.authenticated().getId()));
        return repository.save(relator);
    }

    public Relator update(Integer id, Relator novo) {
        Relator atual = findById(id);
        atual.setNome(novo.getNome());
        atual.setCpf(novo.getCpf());
        atual.setCidade(novo.getCidade());
        atual.setEstado(novo.getEstado());
        return repository.save(atual);
    }

    public void delete(Integer id) { repository.delete(findById(id)); }
}
