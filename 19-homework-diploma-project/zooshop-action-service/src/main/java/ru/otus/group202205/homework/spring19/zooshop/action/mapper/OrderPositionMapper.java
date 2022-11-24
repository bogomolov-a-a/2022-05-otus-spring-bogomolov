package ru.otus.group202205.homework.spring19.zooshop.action.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.otus.group202205.homework.spring19.zooshop.action.dto.ActionDto;
import ru.otus.group202205.homework.spring19.zooshop.action.model.Action;

@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    implementationPackage = "ru.otus.group202205.homework.spring19.zooshop.action.mapper.impl")
public interface OrderPositionMapper {

  ActionDto toDto(Action action);

  Action toEntity(ActionDto address);

}
