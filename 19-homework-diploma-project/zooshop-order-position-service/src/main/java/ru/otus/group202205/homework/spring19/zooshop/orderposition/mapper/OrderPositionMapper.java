package ru.otus.group202205.homework.spring19.zooshop.orderposition.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.otus.group202205.homework.spring19.zooshop.orderposition.dto.OrderPositionDto;
import ru.otus.group202205.homework.spring19.zooshop.orderposition.model.OrderPosition;

@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    implementationPackage = "ru.otus.group202205.homework.spring19.zooshop.orderposition.mapper.impl")
public interface OrderPositionMapper {

  OrderPositionDto toDto(OrderPosition orderPosition);

  OrderPosition toEntity(OrderPositionDto address);

}
