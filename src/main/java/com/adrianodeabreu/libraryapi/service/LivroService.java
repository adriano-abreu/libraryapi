package com.adrianodeabreu.libraryapi.service;

import com.adrianodeabreu.libraryapi.model.GeneroLivro;
import com.adrianodeabreu.libraryapi.model.Livro;
import com.adrianodeabreu.libraryapi.repository.LivroRepository;
import com.adrianodeabreu.libraryapi.repository.specs.LivroSpecs;
import com.adrianodeabreu.libraryapi.validator.LivroValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository repository;

    private final LivroValidator livroValidator;

    public Livro salvar(Livro livro) {
        livroValidator.validar(livro);
        return  repository.save(livro);
    }

    public Optional<Livro> buscarPorId(UUID id) {
        return repository.findById(id);
    }

    public void excluir(UUID id) {
        repository.deleteById(id);
    }

    public Page<Livro> pesquisar(String isbn, String titulo, String nomeAutor, GeneroLivro genero, Integer anoPublicacao, Integer pagina, Integer tamanhoPagina) {
//        Specification<Livro> specs = Specification
//                .where(LivroSpecs.isbnEqual(isbn))
//                .and(LivroSpecs.tituloLike(titulo))
//                .and(LivroSpecs.generoEqual(genero));

        Specification<Livro> specs = LivroSpecs.builder()
                .comIsbn(isbn)
                .comTitulo(titulo)
                .comGenero(genero)
                .comAnoPublicacao(anoPublicacao)
                .comNomeAutor(nomeAutor)
                .build();

        Pageable pagerequest = PageRequest.of(pagina, tamanhoPagina);

        return repository.findAll(specs, pagerequest);
    }

    public void atualizar(Livro livroAtualizado) {
        if (livroAtualizado.getId() == null) {
             throw new IllegalArgumentException("O livro não pode ser nulo.");
        }

        livroValidator.validar(livroAtualizado);
        repository.save(livroAtualizado);
    }
}
