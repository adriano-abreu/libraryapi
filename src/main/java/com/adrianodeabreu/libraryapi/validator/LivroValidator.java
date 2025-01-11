package com.adrianodeabreu.libraryapi.validator;

import com.adrianodeabreu.libraryapi.exceptions.CampoInvalidoException;
import com.adrianodeabreu.libraryapi.exceptions.RegistroDuplicadoException;
import com.adrianodeabreu.libraryapi.model.Livro;
import com.adrianodeabreu.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LivroValidator {


    private static final int MINIMO_ANO_PUBLICACAO = 2020;

    private final LivroRepository repository;

    public void validar(Livro livro) {
        if (existeLivroCadastrado(livro)) {
            throw new RegistroDuplicadoException("Já existe um livro com este ISBN");
        }

        if(isPrecoObrigatorioNullo(livro)) {
            throw new CampoInvalidoException("preco", "Para livros com o ano de publicação a partir de 2020, o campo Preco é obrigatório.");
        }
    }

    private boolean isPrecoObrigatorioNullo(Livro livro) {
        return livro.getPreco() == null && livro.getDataPublicacao().getYear() >= MINIMO_ANO_PUBLICACAO;
    }

    private boolean existeLivroCadastrado(Livro livro) {
        Optional<Livro> resultado = repository.findByIsbn(livro.getIsbn());


        return resultado
                .map(Livro::getId)
                .filter(id -> !id.equals(livro.getId()))
                .isPresent();
    }
}
