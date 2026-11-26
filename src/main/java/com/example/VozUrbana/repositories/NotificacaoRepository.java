package com.example.VozUrbana.repositories;

import java.util.List;
import com.example.VozUrbana.models.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {
    List<Notificacao> findByUsuario_IdUsuario(Integer idUsuario);
    List<Notificacao> findByDenuncia_IdDenuncia(Integer idDenuncia);
    List<Notificacao> findByDenuncia_IdDenunciaAndUsuario_IdUsuario(Integer idDenuncia, Integer idUsuario);
}
