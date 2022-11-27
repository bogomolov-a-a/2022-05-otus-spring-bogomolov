package ru.otus.group202205.homework.spring19.zooshop.category.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.otus.group202205.homework.spring19.zooshop.category.dto.CategoryDto;
import ru.otus.group202205.homework.spring19.zooshop.category.model.Category;

@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    implementationPackage = "ru.otus.group202205.homework.spring19.zooshop.category.mapper.impl")
public interface CategoryMapper {

  CategoryDto toDto(Category category);

  Category toEntity(CategoryDto address);

}
