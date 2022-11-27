package ru.otus.group202205.homework.spring19.zooshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
public class ZooShopConfigServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ZooShopConfigServerApplication.class,
        args);
  }

}
