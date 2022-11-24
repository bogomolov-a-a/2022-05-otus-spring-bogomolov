package ru.otus.group202205.homework.spring19.zooshop.producer.service.impl;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring19.zooshop.producer.dao.ProducerRepository;
import ru.otus.group202205.homework.spring19.zooshop.producer.dto.ProducerDto;
import ru.otus.group202205.homework.spring19.zooshop.producer.feign.AddressService;
import ru.otus.group202205.homework.spring19.zooshop.producer.feign.GoodService;
import ru.otus.group202205.homework.spring19.zooshop.producer.mapper.ProducerMapper;
import ru.otus.group202205.homework.spring19.zooshop.producer.model.Producer;
import ru.otus.group202205.homework.spring19.zooshop.producer.service.ProducerService;

@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {

  private final ProducerRepository producerRepository;
  private final ProducerMapper producerMapper;
  private final GoodService producerServiceFeignProxy;
  private final AddressService addressService;

  @Override
  public List<ProducerDto> findAll(Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return producerRepository
        .findAll(pageable)
        .stream()
        .map(producerMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public ProducerDto findById(Long id) {
    return producerMapper.toDto(producerRepository
        .findById(id)
        .orElseThrow());
  }

  @Override
  public List<ProducerDto> findAllByName(String name, Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return producerRepository
        .findAllByName(name,
            pageable)
        .stream()
        .map(producerMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<ProducerDto> findAllByAddressId(Long addressId, Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return producerRepository
        .findAllByAddressId(addressId,
            pageable)
        .stream()
        .map(producerMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public ProducerDto save(ProducerDto producer) {
    addressService.existsById(producer.getAddressId());
    return producerMapper.toDto(producerRepository.save(producerMapper.toEntity(producer)));
  }

  @Override
  public void deleteById(Long id) {
    producerServiceFeignProxy.deleteAllByProducer(id);
    producerRepository.deleteById(id);
  }

  @Override
  public void deleteAll() {
    producerRepository
        .findAll()
        .forEach(producer -> this.deleteById(producer.getId()));
  }

  @Override
  public void deleteAllByAddressId(Long addressId) {
    List<Producer> producers = producerRepository
        .findAllByAddressId(addressId,
            Pageable.unpaged())
        .getContent();
    producerRepository.deleteAll(producers);
  }

  @Override
  public boolean existsById(Long id) {
    return producerRepository.existsById(id);
  }

}
