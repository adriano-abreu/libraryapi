package com.adrianodeabreu.libraryapi.controller.dto;

import com.adrianodeabreu.libraryapi.model.GeneroLivro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CadastroLivroDTO(
        @ISBN(message = "O ISBN é inválido.")
        @NotBlank(message = "O ISBN é obrigatório.")
        String isbn,

        @NotBlank(message = "O título é obrigatório.")
        String titulo,

        @NotNull(message = "A data de publicação é obrigatória.")
        @Past(message = "A data de publicação deve ser no passado.")
        LocalDate dataPublicacao,

        GeneroLivro genero,

        BigDecimal preco,

        @NotNull(message = "O id do autor é obrigatório.")
        UUID idAutor
        ) {
}
