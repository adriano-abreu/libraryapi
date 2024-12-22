package com.adrianodeabreu.libraryapi.service;

import com.adrianodeabreu.libraryapi.model.Livro;
import com.adrianodeabreu.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository repository;

    public Livro salvar(Livro livro) {
      return  repository.save(livro);
    }
}
