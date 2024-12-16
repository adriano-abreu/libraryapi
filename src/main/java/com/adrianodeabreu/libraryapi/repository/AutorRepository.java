package com.adrianodeabreu.libraryapi.repository;

import com.adrianodeabreu.libraryapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {

    List<Autor> findByNomeAndNacionalidade(String nome, String nacionalidade);
    List<Autor> findByNome(String nome);
    List<Autor> findByNacionalidade(String nacionalidade);

}
