package ru.otus.group202205.homework.spring19.zooshop.customer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "zooshop-order-service")
public interface OrderService {

  @DeleteMapping("/by/customer/{customerId}")
  void deleteAllByCustomer(@PathVariable("customerId") Long customerId);

}
