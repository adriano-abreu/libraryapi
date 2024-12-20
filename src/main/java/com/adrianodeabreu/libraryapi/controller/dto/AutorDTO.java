package com.adrianodeabreu.libraryapi.controller.dto;

import com.adrianodeabreu.libraryapi.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;



import java.time.LocalDate;
import java.util.UUID;

public record AutorDTO(
        UUID id,
        @NotBlank(message = "O nome do autor é obrigatório")
        @Size(min = 3, max = 100, message = "O nome do autor deve ter no máximo 100 caracteres")
        String nome,
        @NotNull(message = "A data de nascimento do autor é obrigatória")
        @Past(message = "A data de nascimento do autor deve ser anterior à data atual ou futura")
        LocalDate dataNascimento,
        @NotBlank(message = "A nacionalidade do autor é obrigatória")
        @Size(min = 3, max = 50, message = "A nacional idade do autor deve ter no máximo 50 caracteres")
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
