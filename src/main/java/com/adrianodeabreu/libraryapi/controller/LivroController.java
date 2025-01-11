package com.adrianodeabreu.libraryapi.controller;

import com.adrianodeabreu.libraryapi.controller.dto.CadastroLivroDTO;
import com.adrianodeabreu.libraryapi.controller.dto.ErroResposta;
import com.adrianodeabreu.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import com.adrianodeabreu.libraryapi.controller.mappers.LivroMapper;
import com.adrianodeabreu.libraryapi.exceptions.RegistroDuplicadoException;
import com.adrianodeabreu.libraryapi.model.GeneroLivro;
import com.adrianodeabreu.libraryapi.model.Livro;
import com.adrianodeabreu.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
public class LivroController implements GenericController {

    private final LivroService service;

    @Qualifier("livroMapperImpl")
    private final LivroMapper mapper;


    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid CadastroLivroDTO dto) {
        try {
            Livro livroEntidade = mapper.toEntity(dto);

            service.salvar(livroEntidade);

            URI location = gerarHeaderLocation(livroEntidade.getId());

            return ResponseEntity.created(location).build();
        } catch (RegistroDuplicadoException e) {
            var erroDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalhesLivros(
            @PathVariable("id") String id) {
        return service.buscarPorId(UUID.fromString(id)).map(
                livro -> {
                    var dto = mapper.toDTO(livro);
                    return ResponseEntity.ok(dto);
                }
        ).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletarLivro(@PathVariable("id") String id) {
        return service.buscarPorId(UUID.fromString(id))
                .map(livro -> {
                    service.excluir(livro.getId());
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ResultadoPesquisaLivroDTO>> pesquisar(
            @RequestParam(value = "isbn", required = false)
            String isbn,
            @RequestParam(value = "titulo", required = false)
            String tiulo,
            @RequestParam(value = "nome-autor", required = false)
            String nome,
            @RequestParam(value = "genero", required = false)
            GeneroLivro genero,
            @RequestParam(value = "ano-publicacao", required = false)
            Integer anoPublicacao
    ) {
        var resultado = service.pesquisar(isbn, tiulo, nome, genero, anoPublicacao);
        return ResponseEntity.ok(resultado.stream().map(mapper::toDTO).collect(Collectors.toList()));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> atualizarLivro(@PathVariable("id") String id, @RequestBody @Valid CadastroLivroDTO dto) {

        return service.buscarPorId(UUID.fromString(id))
                .map(livro -> {
                    Livro livroAtualizado = mapper.toEntity(dto);
                    livro.setDataPublicacao(livro.getDataPublicacao());
                    livro.setIsbn(livroAtualizado.getIsbn());
                    livro.setPreco(livroAtualizado.getPreco());
                    livro.setTitulo(livroAtualizado.getTitulo());
                    livro.setGenero(livroAtualizado.getGenero());
                    livro.setAutor(livroAtualizado.getAutor());
                    service.atualizar(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
