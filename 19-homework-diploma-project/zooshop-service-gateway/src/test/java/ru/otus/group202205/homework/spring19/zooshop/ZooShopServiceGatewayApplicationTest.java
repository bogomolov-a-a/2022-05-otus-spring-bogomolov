package ru.otus.group202205.homework.spring19.zooshop;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ZooShopServiceGatewayApplicationTest {

  @Test
  void contextLoad(@LocalServerPort Integer port) {
    assertThat(port)
        .isNotNull()
        .isBetween(1,
            65355);

  }

}