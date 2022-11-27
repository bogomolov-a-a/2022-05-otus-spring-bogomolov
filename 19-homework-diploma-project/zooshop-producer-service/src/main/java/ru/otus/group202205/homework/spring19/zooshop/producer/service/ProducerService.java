package ru.otus.group202205.homework.spring19.zooshop.producer.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.group202205.homework.spring19.zooshop.producer.dto.ProducerDto;

public interface ProducerService {

  List<ProducerDto> findAll(Pageable pageable, Sort sort);

  ProducerDto findById(Long id);

  List<ProducerDto> findAllByName(String name, Pageable pageable, Sort sort);

  List<ProducerDto> findAllByAddressId(Long addressId, Pageable pageable, Sort sort);

  ProducerDto save(ProducerDto good);

  void deleteById(Long id);

  void deleteAll();

  void deleteAllByAddressId(Long addressId);

  boolean existsById(Long id);

}
