package ru.otus.group202205.homework.spring19.zooshop.producer.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.otus.group202205.homework.spring19.zooshop.producer.dto.ProducerDto;
import ru.otus.group202205.homework.spring19.zooshop.producer.model.Producer;

@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    implementationPackage = "ru.otus.group202205.homework.spring19.zooshop.producer.mapper.impl")
public interface ProducerMapper {

  ProducerDto toDto(Producer producer);

  Producer toEntity(ProducerDto good);

}
