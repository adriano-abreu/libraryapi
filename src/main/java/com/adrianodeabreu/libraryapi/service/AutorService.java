package com.adrianodeabreu.libraryapi.service;

import com.adrianodeabreu.libraryapi.config.validator.AutorValidator;
import com.adrianodeabreu.libraryapi.exceptions.OperacaoNaoPermitidaException;
import com.adrianodeabreu.libraryapi.model.Autor;
import com.adrianodeabreu.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AutorService {

    private final AutorRepository repository;

    private final AutorValidator validator;

    public AutorService(AutorRepository repository, AutorValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public void salvar(Autor autor) {
        validator.validar(autor);
        repository.save(autor);
    }

    public void atualizar(Autor autor) {
        if (autor.getId() == null) {
            throw new IllegalArgumentException("O autor não possui um ID");
        }
        validator.validar(autor);
        repository.save(autor);
    }

    public Optional<Autor> buscarPorId(UUID id) {
       return repository.findById(id);
    }

    public void deletar(Autor autor) {
        if (possuiLivro(autor)){
            throw new OperacaoNaoPermitidaException("Não é possível excluir um autor que possui livros cadastrados.");
        }
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

    public boolean possuiLivro(Autor autor){
        return autor.getLivros().isEmpty();
    }
}
