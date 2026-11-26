package com.example.VozUrbana.repositories;

import java.util.List;
import java.util.Optional;
import com.example.VozUrbana.models.Usuario;
import com.example.VozUrbana.projection.UsuarioSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    Optional<UsuarioSummary> findProjectedByIdUsuario(Integer idUsuario);
    List<UsuarioSummary> findAllProjectedBy();
}
