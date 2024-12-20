package com.adrianodeabreu.libraryapi.controller;

import com.adrianodeabreu.libraryapi.controller.dto.AutorDTO;

import com.adrianodeabreu.libraryapi.controller.dto.ErroResposta;
import com.adrianodeabreu.libraryapi.exceptions.OperacaoNaoPermitidaException;
import com.adrianodeabreu.libraryapi.exceptions.RegistroDuplicadoException;
import com.adrianodeabreu.libraryapi.model.Autor;
import com.adrianodeabreu.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("autores")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService service;

    @PostMapping
    public ResponseEntity<Object> salvarAutor(@RequestBody @Valid AutorDTO autor) {
        try {
            Autor autorEntidade = autor.mapearParaAutor();
            service.salvar(autorEntidade);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(autorEntidade.getId()).toUri();

            return ResponseEntity.created(location).build();
        }
        catch (RegistroDuplicadoException e) {
            var erroDTO = ErroResposta.conflito(e.getMessage(), List.of());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<AutorDTO> buscarAutorPorId(@PathVariable("id") String id) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.buscarPorId(idAutor);

        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Autor autor = autorOptional.get();

        AutorDTO autorDTO = new AutorDTO(
                autor.getId(),
                autor.getNome(),
                autor.getDataNascimento(),
                autor.getNacionalidade()
        );

        return ResponseEntity.ok(autorDTO);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletarAutor(@PathVariable("id") String id) {
       try {
           var idAutor = UUID.fromString(id);
           Optional<Autor> autorOptional = service.buscarPorId(idAutor);
           if (autorOptional.isEmpty()) {
               return ResponseEntity.notFound().build();
           }
           Autor autor = autorOptional.get();
           service.deletar(autor);
           return ResponseEntity.noContent().build();
       } catch (OperacaoNaoPermitidaException e) {
           var erroDTO = ErroResposta.repostaPadrao(e.getMessage(), List.of());
           return ResponseEntity.status(erroDTO.status()).body(erroDTO);
       }
    }

    @GetMapping
    public ResponseEntity<List<AutorDTO>> pesquisarAutoresPorNomeOuNacionalidade(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade
    ) {
        List<Autor> autores = service.pesquisaByExample(nome, nacionalidade);
        
        List<AutorDTO> autorDTOS = autores.stream().map(autor -> new AutorDTO(
                autor.getId(),
                autor.getNome(),
                autor.getDataNascimento(),
                autor.getNacionalidade()
        )).toList();
        return ResponseEntity.ok(autorDTOS);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> atualizarAutor(@PathVariable("id") String id, @RequestBody @Valid AutorDTO autorDTO) {
        try {
            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = service.buscarPorId(idAutor);
            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Autor autor = autorOptional.get();
            autor.setNome(autorDTO.nome());
            autor.setDataNascimento(autorDTO.dataNascimento());
            autor.setNacionalidade(autorDTO.nacionalidade());
            service.atualizar(autor);
            return ResponseEntity.noContent().build();
        } catch (RegistroDuplicadoException e) {
            var erroDTO = ErroResposta.conflito(e.getMessage(), List.of());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }
}
