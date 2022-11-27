package ru.otus.group202205.homework.spring19.zooshop.orderposition.health;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring19.zooshop.orderposition.dao.OrderPositionRepository;

@Component
@RequiredArgsConstructor
public class DbNotEmptyHealthIndicator implements HealthIndicator {

  private final OrderPositionRepository orderPositionRepository;

  @Override
  public Health health() {
    if (orderPositionRepository.exists() != null) {
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
