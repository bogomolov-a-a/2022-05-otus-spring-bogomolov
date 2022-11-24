package ru.otus.group202205.homework.spring12.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.otus.group202205.homework.spring12.dto.GenreDto;
import ru.otus.group202205.homework.spring12.model.Genre;

@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    implementationPackage = "ru.otus.group202205.homework.spring12.service.mapper.impl")
public interface GenreMapper {

  Genre toEntity(GenreDto genreDto);

  GenreDto toDto(Genre genre);

}
