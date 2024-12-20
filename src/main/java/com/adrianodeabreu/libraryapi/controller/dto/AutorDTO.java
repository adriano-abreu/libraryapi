package com.adrianodeabreu.libraryapi.controller.dto;

import com.adrianodeabreu.libraryapi.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;
import java.util.UUID;

public record AutorDTO(
        UUID id,
        @NotBlank(message = "O nome do autor é obrigatório")
        String nome,
        @NotNull(message = "A data de nascimento do autor é obrigatória")
        LocalDate dataNascimento,
        @NotBlank(message = "A nacionalidade do autor é obrigatória")
        String nacionalidade) {

    @NotNull
    public Autor mapearParaAutor() {
        Autor autor = new Autor();
        autor.setNome(this.nome);
        autor.setDataNascimento(this.dataNascimento);
        autor.setNacionalidade(this.nacionalidade);
        return autor;
    }
}
