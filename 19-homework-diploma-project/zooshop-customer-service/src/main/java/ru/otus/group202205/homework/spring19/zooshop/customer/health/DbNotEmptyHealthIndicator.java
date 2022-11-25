package ru.otus.group202205.homework.spring19.zooshop.customer.health;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring19.zooshop.customer.dao.CustomerRepository;

@Component
@RequiredArgsConstructor
public class DbNotEmptyHealthIndicator implements HealthIndicator {

  private final CustomerRepository customerRepository;

  @Override
  public Health health() {
    if (customerRepository.exists() != null) {
      return Health
          .up()
          .build();
    }

    return Health
        .outOfService()
        .withDetail("table_row",
            0L)
        .build();
  }

}
