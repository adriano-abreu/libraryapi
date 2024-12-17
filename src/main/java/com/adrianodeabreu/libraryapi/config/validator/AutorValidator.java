package com.adrianodeabreu.libraryapi.config.validator;

import com.adrianodeabreu.libraryapi.exceptions.RegistroDuplicadoException;
import com.adrianodeabreu.libraryapi.model.Autor;
import com.adrianodeabreu.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Component;

@Component
public class AutorValidator {

    private AutorRepository repository;

    public AutorValidator(AutorRepository repository) {
        this.repository = repository;
    }

    public void validar(Autor autor) {
        if (!existeAutorCadastrado(autor)) {
            throw new RegistroDuplicadoException("Já existe um autor com esse nome e nacionalidade");
        }
    }

    private boolean existeAutorCadastrado(Autor autor) {
        var nome = autor.getNome();
        var dataNascimento = autor.getDataNascimento();
        var nacionalidade = autor.getNacionalidade();
        var autorExistente = repository.findByNomeAndDataNascimentoAndNacionalidade(nome, dataNascimento, nacionalidade);
        return autorExistente.get().getId() == autor.getId();
    }
}
