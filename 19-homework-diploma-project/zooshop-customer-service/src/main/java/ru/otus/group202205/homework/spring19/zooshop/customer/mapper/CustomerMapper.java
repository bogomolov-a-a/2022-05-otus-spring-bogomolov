package ru.otus.group202205.homework.spring19.zooshop.customer.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.otus.group202205.homework.spring19.zooshop.customer.dto.CustomerDto;
import ru.otus.group202205.homework.spring19.zooshop.customer.model.Customer;

@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    implementationPackage = "ru.otus.group202205.homework.spring19.zooshop.customer.mapper.impl")
public interface CustomerMapper {

  CustomerDto toDto(Customer customer);

  Customer toEntity(CustomerDto good);

}
