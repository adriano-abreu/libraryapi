package com.adrianodeabreu.libraryapi.controller.dto;

import com.adrianodeabreu.libraryapi.model.Autor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record AutorDTO(UUID id, String nome, LocalDate dataNascimento, String nacionalidade) {

    @NotNull
    public Autor mapearParaAutor() {
        Autor autor = new Autor();
        autor.setNome(this.nome);
        autor.setDataNascimento(this.dataNascimento);
        autor.setNacionalidade(this.nacionalidade);
        return autor;
    }
}
