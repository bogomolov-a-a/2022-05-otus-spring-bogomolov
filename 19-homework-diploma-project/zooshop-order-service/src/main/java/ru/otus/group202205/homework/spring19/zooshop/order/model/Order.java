package ru.otus.group202205.homework.spring19.zooshop.order.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "orders")
@Getter
@Setter
@EqualsAndHashCode
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NonNull
  private Long customerId;
  @NonNull
  @Column(columnDefinition = "TIMESTAMP")
  private LocalDateTime orderDate;
  private Long deliveredAddressId;
  private String description;
  @Column(columnDefinition = "TIMESTAMP")
  private LocalDate deliveredDate;
  @Enumerated(EnumType.STRING)
  @NonNull
  private OrderStatus orderStatus = OrderStatus.CREATED;

}
