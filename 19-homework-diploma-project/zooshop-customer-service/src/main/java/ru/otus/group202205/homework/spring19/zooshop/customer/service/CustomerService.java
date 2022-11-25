package ru.otus.group202205.homework.spring19.zooshop.customer.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.group202205.homework.spring19.zooshop.customer.dto.CustomerDto;

public interface CustomerService {

  List<CustomerDto> findAll(Pageable pageable, Sort sort);

  CustomerDto findById(Long id);

  List<CustomerDto> findAllByAddressId(Long addressId, Pageable pageable, Sort sort);

  CustomerDto save(CustomerDto good);

  void deleteById(Long id);

  void deleteAll();

  void deleteAllByAddressId(Long addressId);

  boolean existsById(Long id);

}
