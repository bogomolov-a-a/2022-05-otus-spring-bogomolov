package ru.otus.group202205.homework.spring19.zooshop.orderposition.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "order_positions")
@Getter
@Setter
@EqualsAndHashCode
public class OrderPosition {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NonNull
  private String name;
  @NonNull
  private Long goodId;
  @NonNull
  private Long orderId;
  @NonNull
  private Long actionId;
  @NonNull
  private Float quantity;

}
