package ru.otus.group202205.homework.spring19.zooshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ZooShopServiceDiscoveryApplication {

  public static void main(String[] args) {
    SpringApplication.run(ZooShopServiceDiscoveryApplication.class,
        args);
  }

}
