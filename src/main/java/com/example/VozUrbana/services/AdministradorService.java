package com.example.VozUrbana.services;

import java.util.List;
import com.example.VozUrbana.models.Administrador;
import com.example.VozUrbana.repositories.AdministradorRepository;
import com.example.VozUrbana.services.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdministradorService {
    private final AdministradorRepository repository;
    private final AcessoService acesso;

    public AdministradorService(AdministradorRepository repository, AcessoService acesso) {
        this.repository = repository;
        this.acesso = acesso;
    }

    public List<Administrador> findAll() {
        acesso.requireAdmin();
        return repository.findAll();
    }
    public Administrador findById(Integer id) {
        acesso.requireAdmin();
        return repository.findById(id).orElseThrow(() ->
            new ObjectNotFoundException("Administrador não encontrado com ID: " + id));
    }
    public Administrador create(Administrador entity) {
        acesso.requireAdmin();
        entity.setIdAdministrador(null);
        return repository.save(entity);
    }
    public Administrador update(Integer id, Administrador novo) {
        Administrador atual = findById(id);
        atual.setNome(novo.getNome());
        atual.setEmail(novo.getEmail());
        atual.setNivelAcesso(novo.getNivelAcesso());
        // Não trocar o usuário dono da conta nem a data original.
        return repository.save(atual);
    }
    public void delete(Integer id) { repository.delete(findById(id)); }
}
