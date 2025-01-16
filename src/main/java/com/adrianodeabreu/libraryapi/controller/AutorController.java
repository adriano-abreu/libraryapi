package com.adrianodeabreu.libraryapi.controller;

import com.adrianodeabreu.libraryapi.controller.dto.AutorDTO;

import com.adrianodeabreu.libraryapi.controller.mappers.AutorMapper;
import com.adrianodeabreu.libraryapi.model.Autor;
import com.adrianodeabreu.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("autores")
@RequiredArgsConstructor
public class  AutorController implements GenericController {

    private final AutorService service;
    @Qualifier("autorMapperImpl")
    private final AutorMapper mapper;

    @PostMapping
    public ResponseEntity<Object> salvarAutor(@RequestBody @Valid AutorDTO dt0) {
        Autor autor = mapper.toEntity(dt0);
        service.salvar(autor);
        URI location = gerarHeaderLocation(autor.getId());
        
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<AutorDTO> buscarAutorPorId(@PathVariable("id") String id) {
        var idAutor = UUID.fromString(id);

        return service
                .buscarPorId(idAutor)
                .map(autor -> ResponseEntity.ok(mapper.toDTO(autor)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletarAutor(@PathVariable("id") String id) {

        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.buscarPorId(idAutor);
        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Autor autor = autorOptional.get();
        service.deletar(autor);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AutorDTO>> pesquisarAutoresPorNomeOuNacionalidade(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade
    ) {
        List<Autor> autores = service.pesquisaByExample(nome, nacionalidade);
        
        List<AutorDTO> autorDTOS = autores
                .stream()
                .map(mapper::toDTO) //.map(autor -> mapper.toDTO(autor)
                .toList();
        return ResponseEntity.ok(autorDTOS);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> atualizarAutor(@PathVariable("id") String id, @RequestBody @Valid AutorDTO autorDTO) {
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
    }
}
