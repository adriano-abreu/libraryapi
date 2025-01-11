package com.adrianodeabreu.libraryapi.repository;

import com.adrianodeabreu.libraryapi.model.Autor;
import com.adrianodeabreu.libraryapi.model.GeneroLivro;
import com.adrianodeabreu.libraryapi.model.Livro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID>, JpaSpecificationExecutor<Livro> {

    boolean existsByAutor(Autor autor);

    Optional<Livro> findByIsbn(String isbn);
}
