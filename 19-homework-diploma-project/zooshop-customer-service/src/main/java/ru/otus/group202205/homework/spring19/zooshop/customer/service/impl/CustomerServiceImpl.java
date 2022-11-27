package ru.otus.group202205.homework.spring19.zooshop.customer.service.impl;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring19.zooshop.customer.dao.CustomerRepository;
import ru.otus.group202205.homework.spring19.zooshop.customer.dto.CustomerDto;
import ru.otus.group202205.homework.spring19.zooshop.customer.feign.AddressService;
import ru.otus.group202205.homework.spring19.zooshop.customer.feign.OrderService;
import ru.otus.group202205.homework.spring19.zooshop.customer.mapper.CustomerMapper;
import ru.otus.group202205.homework.spring19.zooshop.customer.model.Customer;
import ru.otus.group202205.homework.spring19.zooshop.customer.service.CustomerService;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;
  private final OrderService orderService;
  private final AddressService addressService;

  @Override
  public List<CustomerDto> findAll(Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return customerRepository
        .findAll(pageable)
        .stream()
        .map(customerMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public CustomerDto findById(Long id) {
    return customerMapper.toDto(customerRepository
        .findById(id)
        .orElseThrow());
  }

  @Override
  public List<CustomerDto> findAllByAddressId(Long addressId, Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return customerRepository
        .findAllByAddressId(addressId,
            pageable)
        .stream()
        .map(customerMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public CustomerDto save(CustomerDto customer) {
    addressService.existsById(customer.getAddressId());
    return customerMapper.toDto(customerRepository.save(customerMapper.toEntity(customer)));
  }

  @Override
  public void deleteById(Long id) {
    orderService.deleteAllByCustomer(id);
    customerRepository.deleteById(id);
  }

  @Override
  public void deleteAll() {
    customerRepository
        .findAll()
        .forEach(customer -> this.deleteById(customer.getId()));
  }

  @Override
  public void deleteAllByAddressId(Long addressId) {
    customerRepository
        .findAllByAddressId(addressId,
            Pageable.unpaged())
        .stream()
        .map(Customer::getId)
        .forEach(this::deleteById);
  }

  @Override
  public boolean existsById(Long id) {
    return customerRepository.existsById(id);
  }

}
