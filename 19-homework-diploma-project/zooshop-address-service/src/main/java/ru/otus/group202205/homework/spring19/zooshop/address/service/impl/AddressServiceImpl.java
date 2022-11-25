package ru.otus.group202205.homework.spring19.zooshop.address.service.impl;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring19.zooshop.address.dao.AddressRepository;
import ru.otus.group202205.homework.spring19.zooshop.address.dto.AddressDto;
import ru.otus.group202205.homework.spring19.zooshop.address.feign.CustomerService;
import ru.otus.group202205.homework.spring19.zooshop.address.feign.OrderService;
import ru.otus.group202205.homework.spring19.zooshop.address.feign.ProducerService;
import ru.otus.group202205.homework.spring19.zooshop.address.mapper.AddressMapper;
import ru.otus.group202205.homework.spring19.zooshop.address.service.AddressService;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

  private final AddressRepository addressRepository;
  private final AddressMapper addressMapper;
  private final ProducerService producerService;
  private final OrderService orderService;
  private final CustomerService customerService;

  @Override
  public List<AddressDto> findAll(Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return addressRepository
        .findAll(pageable)
        .stream()
        .map(addressMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public AddressDto findById(Long id) {
    return addressMapper.toDto(addressRepository
        .findById(id)
        .orElseThrow());
  }

  @Override
  public List<AddressDto> findAllBySomeText(String text, Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return addressRepository
        .findAllBySomeText(text,
            pageable)
        .stream()
        .map(addressMapper::toDto)
        .collect(Collectors.toList());
  }


  @Override
  public AddressDto save(AddressDto address) {
    return addressMapper.toDto(addressRepository.save(addressMapper.toEntity(address)));
  }

  @Override
  public void deleteById(Long id) {
    producerService.deleteAllByAddress(id);
    orderService.deleteAllByAddress(id);
    customerService.deleteAllByAddress(id);
    addressRepository.deleteById(id);
  }

  @Override
  public void deleteAll() {
    addressRepository
        .findAll()
        .forEach(address -> this.deleteById(address.getId()));
  }

  @Override
  public boolean existsById(Long id) {
    return addressRepository.existsById(id);
  }

}
