package com.adrianodeabreu.libraryapi.repository;

import com.adrianodeabreu.libraryapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {
}
