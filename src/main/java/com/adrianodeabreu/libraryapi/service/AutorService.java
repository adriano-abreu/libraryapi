package com.adrianodeabreu.libraryapi.service;

import com.adrianodeabreu.libraryapi.model.Autor;
import com.adrianodeabreu.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AutorService {

    private final AutorRepository repository;

    public AutorService(AutorRepository repository) {
        this.repository = repository;
    }

    public void salvar(Autor autor) {
        repository.save(autor);
    }

    public void atualizar(Autor autor) {
        if (autor.getId() == null) {
            throw new IllegalArgumentException("O autor não possui um ID");
        }
        repository.save(autor);
    }

    public Optional<Autor> buscarPorId(UUID id) {
       return repository.findById(id);
    }

    public void deletar(Autor autor) {
        repository.delete(autor);
    }

    public List<Autor> pesquisarAutores(String nome, String nacionalidade){
       if (nome != null && nacionalidade != null) {
           return repository.findByNomeAndNacionalidade(nome, nacionalidade);
       }

       if (nome != null) {
           return repository.findByNome(nome);
       }

       if (nacionalidade != null) {
           return repository.findByNacionalidade(nacionalidade);
       }

       return repository.findAll();
    }
}
