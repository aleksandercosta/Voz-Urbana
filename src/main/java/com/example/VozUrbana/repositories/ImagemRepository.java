package com.example.VozUrbana.repositories;

import java.util.List;
import com.example.VozUrbana.models.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagemRepository extends JpaRepository<Imagem, Integer> {
    List<Imagem> findByDenuncia_IdDenuncia(Integer idDenuncia);
    List<Imagem> findByDenuncia_Relator_Usuario_IdUsuario(Integer idUsuario);
}
