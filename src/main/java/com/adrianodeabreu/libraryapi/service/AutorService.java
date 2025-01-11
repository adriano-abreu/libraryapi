package com.adrianodeabreu.libraryapi.service;

import com.adrianodeabreu.libraryapi.validator.AutorValidator;
import com.adrianodeabreu.libraryapi.exceptions.OperacaoNaoPermitidaException;
import com.adrianodeabreu.libraryapi.model.Autor;
import com.adrianodeabreu.libraryapi.repository.AutorRepository;
import com.adrianodeabreu.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository repository;
    private final AutorValidator validator;
    private final LivroRepository livroRepository;

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

    public List<Autor> pesquisaByExample(String nome, String nacionalidade){
        var autor = new Autor();
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Autor> example = Example.of(autor, matcher);

        return repository.findAll(example);
    }

    public boolean possuiLivro(Autor autor){
        return livroRepository.existsByAutor(autor);
    }
}
