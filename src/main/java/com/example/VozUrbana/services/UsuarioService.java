package com.example.VozUrbana.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.example.VozUrbana.dto.UsuarioCreateDTO;
import com.example.VozUrbana.dto.UsuarioResponseDTO;
import com.example.VozUrbana.dto.UsuarioUpdateDTO;
import com.example.VozUrbana.enums.ProfileEnum;
import com.example.VozUrbana.models.Usuario;
import com.example.VozUrbana.projection.UsuarioSummary;
import com.example.VozUrbana.repositories.UsuarioRepository;
import com.example.VozUrbana.services.exceptions.ObjectNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AcessoService acesso;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder,
                          AcessoService acesso) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.acesso = acesso;
    }

    @Transactional(readOnly = true)
    public List<UsuarioSummary> findAll() {
        acesso.requireAdmin();
        return repository.findAllProjectedBy();
    }

    @Transactional(readOnly = true)
    public List<UsuarioSummary> findOwnSummary() {
        Integer id = acesso.authenticated().getId();
        return repository.findProjectedByIdUsuario(id).stream().toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO currentUser() {
        return UsuarioResponseDTO.from(getEntity(acesso.authenticated().getId()));
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO findById(Integer id) {
        acesso.checkOwnerOrAdmin(id);
        return UsuarioResponseDTO.from(getEntity(id));
    }

    private Usuario getEntity(Integer id) {
        return repository.findById(id).orElseThrow(() ->
            new ObjectNotFoundException("Usuário não encontrado com ID: " + id));
    }

    public UsuarioResponseDTO create(UsuarioCreateDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome().trim());
        usuario.setEmail(dto.email().trim());
        usuario.setCpf(dto.cpf());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setProfiles(new HashSet<>(Set.of(ProfileEnum.USER)));
        return UsuarioResponseDTO.from(repository.save(usuario));
    }

    @Transactional
    public UsuarioResponseDTO update(Integer id, UsuarioUpdateDTO dto) {
        acesso.checkOwnerOrAdmin(id);
        Usuario usuario = getEntity(id);
        if (dto.nome() != null) usuario.setNome(dto.nome().trim());
        if (dto.email() != null) usuario.setEmail(dto.email().trim());
        if (dto.cpf() != null) usuario.setCpf(dto.cpf());
        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.senha()));
        }
        return UsuarioResponseDTO.from(repository.save(usuario));
    }

    public void delete(Integer id) {
        acesso.checkOwnerOrAdmin(id);
        Usuario usuario = getEntity(id);
        repository.delete(usuario);
    }

    public void checkAccess(Integer id) {
        acesso.checkOwnerOrAdmin(id);
    }
}
