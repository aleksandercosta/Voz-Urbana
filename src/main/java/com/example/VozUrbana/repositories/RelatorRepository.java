package com.example.VozUrbana.repositories;

import java.util.List;
import com.example.VozUrbana.models.Relator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelatorRepository extends JpaRepository<Relator, Integer> {
    List<Relator> findByUsuario_IdUsuario(Integer idUsuario);
}
