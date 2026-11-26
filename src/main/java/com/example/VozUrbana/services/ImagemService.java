package com.example.VozUrbana.services;

import java.util.List;
import com.example.VozUrbana.models.Imagem;
import com.example.VozUrbana.repositories.ImagemRepository;
import com.example.VozUrbana.services.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ImagemService {
    private final ImagemRepository repository;
    private final DenunciaService denunciaService;
    private final AcessoService acesso;

    public ImagemService(ImagemRepository repository, DenunciaService denunciaService, AcessoService acesso) {
        this.repository = repository;
        this.denunciaService = denunciaService;
        this.acesso = acesso;
    }

    public List<Imagem> findAll() {
        return acesso.isAdmin() ? repository.findAll() :
            repository.findByDenuncia_Relator_Usuario_IdUsuario(acesso.authenticated().getId());
    }
    public Imagem findById(Integer id) {
        Imagem imagem = repository.findById(id).orElseThrow(() ->
            new ObjectNotFoundException("Imagem não encontrada com ID: " + id));
        denunciaService.findById(imagem.getDenuncia().getIdDenuncia());
        return imagem;
    }
    public Imagem create(Imagem imagem) {
        if (imagem.getDenuncia() == null || imagem.getDenuncia().getIdDenuncia() == null)
            throw new IllegalArgumentException("Informe a denúncia");
        imagem.setIdImagem(null);
        imagem.setDenuncia(denunciaService.findById(imagem.getDenuncia().getIdDenuncia()));
        return repository.save(imagem);
    }
    public Imagem update(Integer id, Imagem novo) {
        Imagem atual = findById(id);
        if (novo.getDenuncia() != null && novo.getDenuncia().getIdDenuncia() != null)
            atual.setDenuncia(denunciaService.findById(novo.getDenuncia().getIdDenuncia()));
        atual.setNomeArquivo(novo.getNomeArquivo());
        atual.setCaminho(novo.getCaminho());
        atual.setFormato(novo.getFormato());
        atual.setDataUpload(novo.getDataUpload());
        return repository.save(atual);
    }
    public void delete(Integer id) { repository.delete(findById(id)); }
    public List<Imagem> findByDenuncia(Integer idDenuncia) {
        denunciaService.findById(idDenuncia);
        return repository.findByDenuncia_IdDenuncia(idDenuncia);
    }
}
