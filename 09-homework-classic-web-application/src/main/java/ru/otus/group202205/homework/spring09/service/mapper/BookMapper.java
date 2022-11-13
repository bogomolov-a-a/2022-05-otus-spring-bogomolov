package ru.otus.group202205.homework.spring09.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.otus.group202205.homework.spring09.dto.BookFullDto;
import ru.otus.group202205.homework.spring09.dto.BookSimpleDto;
import ru.otus.group202205.homework.spring09.model.Book;

@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    implementationPackage = "ru.otus.group202205.homework.spring09.service.mapper.impl")
public interface BookMapper {

  Book toEntityFromFull(BookFullDto bookDto);

  BookFullDto toFullDto(Book book);

  BookSimpleDto toSimpleDto(Book book);

  Book toEntityFromSimple(BookSimpleDto bookDto);

}
