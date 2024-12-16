package com.adrianodeabreu.libraryapi.service;

import com.adrianodeabreu.libraryapi.model.Autor;
import com.adrianodeabreu.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Service;

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

    public Optional<Autor> buscarPorId(UUID id) {
       return repository.findById(id);
    }
}
