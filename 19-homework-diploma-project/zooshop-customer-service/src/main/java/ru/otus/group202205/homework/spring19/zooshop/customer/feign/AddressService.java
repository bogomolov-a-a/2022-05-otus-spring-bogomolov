package ru.otus.group202205.homework.spring19.zooshop.customer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("zooshop-address-service")
public interface AddressService {

  @GetMapping("/existence/{id}")
  void existsById(@PathVariable("id") Long producerId);

}
