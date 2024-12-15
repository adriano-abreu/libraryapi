package com.adrianodeabreu.libraryapi.repository;

import com.adrianodeabreu.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {
}
