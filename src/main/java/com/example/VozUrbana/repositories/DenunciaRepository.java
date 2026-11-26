package com.example.VozUrbana.repositories;

import com.example.VozUrbana.models.Denuncia;
import com.example.VozUrbana.projection.DenunciaSummary;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Integer> {
    List<Denuncia> findByRelator_IdRelator(Integer idRelator);
    List<Denuncia> findByAdministrador_IdAdministrador(Integer idAdministrador);
    List<Denuncia> findByRelator_Usuario_IdUsuario(Integer idUsuario);
    List<DenunciaSummary> findAllProjectedBy();
    List<DenunciaSummary> findSummaryByRelator_Usuario_IdUsuario(Integer idUsuario);
}
