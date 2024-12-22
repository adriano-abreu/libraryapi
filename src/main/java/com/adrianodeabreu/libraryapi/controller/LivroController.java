package com.adrianodeabreu.libraryapi.controller;

import com.adrianodeabreu.libraryapi.controller.dto.CadastroLivroDTO;
import com.adrianodeabreu.libraryapi.controller.dto.ErroResposta;
import com.adrianodeabreu.libraryapi.controller.mappers.LivroMapper;
import com.adrianodeabreu.libraryapi.exceptions.RegistroDuplicadoException;
import com.adrianodeabreu.libraryapi.model.Livro;
import com.adrianodeabreu.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService service;

    @Qualifier("livroMapperImpl")
    private final LivroMapper mapper;


    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid CadastroLivroDTO dto) {
        try {
            // mapear dto para entidade
            Livro livroEntidade = mapper.toEntity(dto); //dto.mapearParaLivro();

            // enviar a entidade para o service validar e salvar na base
            service.salvar(livroEntidade);

            // criar url para acesso dos dados do livro
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(livroEntidade.getId()).toUri();

            // retornar 201 created com a url de acesso ao recurso
            return ResponseEntity.created(location).build();
        } catch (RegistroDuplicadoException e) {
           var erroDTO = ErroResposta.conflito(e.getMessage(), List.of());
              return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }
}
