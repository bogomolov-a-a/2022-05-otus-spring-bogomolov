package ru.otus.group202205.homework.spring19.zooshop.address.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.otus.group202205.homework.spring19.zooshop.address.dto.AddressDto;
import ru.otus.group202205.homework.spring19.zooshop.address.model.Address;

@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    implementationPackage = "ru.otus.group202205.homework.spring19.zooshop.address.mapper.impl")
public interface AddressMapper {

  AddressDto toDto(Address address);

  Address toEntity(AddressDto address);

}
