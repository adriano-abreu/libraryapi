package com.adrianodeabreu.libraryapi.repository.specs;

import com.adrianodeabreu.libraryapi.model.GeneroLivro;
import com.adrianodeabreu.libraryapi.model.Livro;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public class LivroSpecs {
    private Specification<Livro> specification;

    private LivroSpecs() {
        this.specification = Specification.where(((root, query, criteriaBuilder) -> criteriaBuilder.conjunction()));
    }

    public static LivroSpecs builder() {
        return new LivroSpecs();
    }

    public LivroSpecs comIsbn(String isbn) {
        return Optional.ofNullable(isbn)
                .map(value -> addSpecification(LivroSpecs.isbnEqual(value)))
                .orElse(this);
    }

    public LivroSpecs comTitulo(String titulo) {
        return Optional.ofNullable(titulo)
                .map(value -> addSpecification(LivroSpecs.tituloLike(value)))
                .orElse(this);
    }

    public LivroSpecs comGenero(GeneroLivro genero) {
        return Optional.ofNullable(genero)
                .map(value -> addSpecification(LivroSpecs.generoEqual(value)))
                .orElse(this);
    }

    public LivroSpecs comAnoPublicacao(Integer anoPublicacao) {
        return Optional.ofNullable(anoPublicacao)
                .map(value -> addSpecification(LivroSpecs.anoPublicacaoEqual(value)))
                .orElse(this);
    }

    public LivroSpecs comNomeAutor(String nomeAutor) {
        return Optional.ofNullable(nomeAutor)
                .map(value -> addSpecification(LivroSpecs.autorNomeLike(value)))
                .orElse(this);
    }

    private LivroSpecs addSpecification(Specification<Livro> newSpec) {
        this.specification = this.specification.and(newSpec);
        return this;
    }

    public Specification<Livro> build() {
        return this.specification;
    }

    public static Specification<Livro> isbnEqual(String isbn) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isbn"), isbn);
    }

    public static Specification<Livro>  tituloLike(String titulo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("titulo")), "%" + titulo.toUpperCase() + "%");
    }

    public static Specification<Livro> generoEqual(GeneroLivro genero) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("genero"), genero);
    }

    public static Specification<Livro> anoPublicacaoEqual(Integer anoPublicacao) {
        // AND to_char(data_publicacao, 'yyyy') = anoPublicacao
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                criteriaBuilder.function("to_char", String.class, root.get("dataPublicacao"), criteriaBuilder.literal("YYYY")), anoPublicacao.toString());
    }

    public static Specification<Livro> autorNomeLike(String nome) {
        return (root, query, criteriaBuilder) -> {
            Join<Object, Object> autor = root.join("autor", JoinType.LEFT);
            return criteriaBuilder.like(criteriaBuilder.upper(autor.get("nome")), "%" + nome.toUpperCase() + "%");
        };

//        return (root, query, criteriaBuilder) -> {
//            query.distinct(true);
//            return criteriaBuilder.like(criteriaBuilder.upper(root.join("autor").get("nome")), "%" + nome.toUpperCase() + "%");
//        };
    }
}
