package ru.otus.group202205.homework.spring06.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.otus.group202205.homework.spring06.dto.AuthorDto;
import ru.otus.group202205.homework.spring06.model.Author;

@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    implementationPackage = "ru.otus.group202205.homework.spring06.service.mapper.impl")
public interface AuthorMapper {

  Author toEntity(AuthorDto authorDto);

  AuthorDto toDto(Author author);

}
