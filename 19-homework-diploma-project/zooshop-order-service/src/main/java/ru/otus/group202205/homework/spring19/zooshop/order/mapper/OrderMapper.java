package ru.otus.group202205.homework.spring19.zooshop.order.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.otus.group202205.homework.spring19.zooshop.order.dto.OrderDto;
import ru.otus.group202205.homework.spring19.zooshop.order.model.Order;

@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    implementationPackage = "ru.otus.group202205.homework.spring19.zooshop.order.mapper.impl")
public interface OrderMapper {

  OrderDto toDto(Order order);

  Order toEntity(OrderDto address);

}
