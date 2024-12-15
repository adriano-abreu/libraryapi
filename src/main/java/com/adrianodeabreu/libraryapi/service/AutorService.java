package com.adrianodeabreu.libraryapi.service;

import com.adrianodeabreu.libraryapi.model.Autor;
import com.adrianodeabreu.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Service;

@Service
public class AutorService {

    private final AutorRepository repository;

    public AutorService(AutorRepository repository) {
        this.repository = repository;
    }

    public Autor salvar(Autor autor) {
        return repository.save(autor);
    }
}
