package ru.otus.group202205.homework.spring19.zooshop.address.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "zooshop-producer-service")
public interface ProducerServiceFeignProxy {

  @GetMapping("/by/address/{addressId}")
  void deleteAllByAddress(@PathVariable("addressId") Long addressId);

}
