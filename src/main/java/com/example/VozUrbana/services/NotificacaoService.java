package com.example.VozUrbana.services;

import java.util.List;
import com.example.VozUrbana.models.Notificacao;
import com.example.VozUrbana.repositories.NotificacaoRepository;
import com.example.VozUrbana.services.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {
    private final NotificacaoRepository repository;
    private final DenunciaService denunciaService;
    private final AcessoService acesso;

    public NotificacaoService(NotificacaoRepository repository, DenunciaService denunciaService, AcessoService acesso) {
        this.repository = repository;
        this.denunciaService = denunciaService;
        this.acesso = acesso;
    }

    public List<Notificacao> findAll() {
        return acesso.isAdmin() ? repository.findAll() :
            repository.findByUsuario_IdUsuario(acesso.authenticated().getId());
    }
    public Notificacao findById(Integer id) {
        Notificacao n = repository.findById(id).orElseThrow(() ->
            new ObjectNotFoundException("Notificação não encontrada com ID: " + id));
        acesso.checkOwnerOrAdmin(n.getUsuario().getIdUsuario());
        return n;
    }
    public Notificacao create(Notificacao n) {
        acesso.requireAdmin();
        n.setIdNotificacao(null);
        return repository.save(n);
    }
    public Notificacao update(Integer id, Notificacao novo) {
        Notificacao atual = findById(id);
        if (!acesso.isAdmin()) {
            // Usuário só pode marcar a própria notificação como visualizada.
            atual.setVisualizada(novo.getVisualizada());
        } else {
            atual.setMensagem(novo.getMensagem());
            atual.setVisualizada(novo.getVisualizada());
            atual.setDataEnvio(novo.getDataEnvio());
        }
        return repository.save(atual);
    }
    public void delete(Integer id) { repository.delete(findById(id)); }
    public List<Notificacao> findByDenuncia(Integer idDenuncia) {
        denunciaService.findById(idDenuncia);
        return acesso.isAdmin() ? repository.findByDenuncia_IdDenuncia(idDenuncia) :
            repository.findByDenuncia_IdDenunciaAndUsuario_IdUsuario(
                idDenuncia, acesso.authenticated().getId());
    }
    public List<Notificacao> findByUsuario(Integer idUsuario) {
        acesso.checkOwnerOrAdmin(idUsuario);
        return repository.findByUsuario_IdUsuario(idUsuario);
    }
}
