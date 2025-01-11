package com.adrianodeabreu.libraryapi.validator;

import com.adrianodeabreu.libraryapi.exceptions.RegistroDuplicadoException;
import com.adrianodeabreu.libraryapi.model.Autor;
import com.adrianodeabreu.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AutorValidator {

    private final AutorRepository repository;

    public AutorValidator(AutorRepository repository) {
        this.repository = repository;
    }

    public void validar(Autor autor) {
        if (existeAutorCadastrado(autor)) {
            throw new RegistroDuplicadoException("Já existe um autor com esse nome e nacionalidade");
        }
    }

    private boolean existeAutorCadastrado(Autor autor) {
        var nome = autor.getNome();
        var dataNascimento = autor.getDataNascimento();
        var nacionalidade = autor.getNacionalidade();
        Optional<Autor> autoEncontrado = repository.findByNomeAndDataNascimentoAndNacionalidade(nome, dataNascimento, nacionalidade);

        if (autor.getId() == null) {
            return autoEncontrado.isPresent();
        }

        return autoEncontrado.isPresent() && !autoEncontrado.get().getId().equals(autor.getId());
    }
}
