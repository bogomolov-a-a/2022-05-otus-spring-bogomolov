package ru.otus.group202205.homework.spring19.zooshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ZooShopServiceGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(ZooShopServiceGatewayApplication.class,
        args);
  }

}
