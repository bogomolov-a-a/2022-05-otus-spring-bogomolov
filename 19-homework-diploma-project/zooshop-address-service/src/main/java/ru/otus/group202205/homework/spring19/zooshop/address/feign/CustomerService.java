package ru.otus.group202205.homework.spring19.zooshop.address.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "zooshop-customer-service")
public interface CustomerService {

  @DeleteMapping("/by/address/{addressId}")
  void deleteAllByAddress(@PathVariable("addressId") Long addressId);

}
