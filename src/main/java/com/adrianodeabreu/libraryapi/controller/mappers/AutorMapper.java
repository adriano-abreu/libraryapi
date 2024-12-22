package com.adrianodeabreu.libraryapi.controller.mappers;

import com.adrianodeabreu.libraryapi.controller.dto.AutorDTO;
import com.adrianodeabreu.libraryapi.model.Autor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "dataNascimento", source = "dataNascimento")
    @Mapping(target = "nacionalidade", source = "nacionalidade")
    Autor toEntity(AutorDTO dto);

    AutorDTO toDTO(Autor autor);
}
