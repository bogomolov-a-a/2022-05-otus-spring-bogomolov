package ru.otus.group202205.homework.spring19.zooshop.address.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.group202205.homework.spring19.zooshop.address.dto.AddressDto;

public interface AddressService {

  List<AddressDto> findAll(Pageable pageable, Sort sort);

  AddressDto findById(Long id);

  List<AddressDto> findAllBySomeText(String text, Pageable pageable, Sort sort);


  AddressDto save(AddressDto good);

  void deleteById(Long id);

  void deleteAll();

  boolean existsById(Long id);

}
