package ru.otus.group202205.homework.spring06.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.otus.group202205.homework.spring06.dto.BookCommentDto;
import ru.otus.group202205.homework.spring06.model.BookComment;

@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    implementationPackage = "ru.otus.group202205.homework.spring06.service.mapper.impl")
public interface BookCommentMapper {

  BookComment toEntity(BookCommentDto bookCommentDto);

  BookCommentDto toDto(BookComment bookComment);

}
