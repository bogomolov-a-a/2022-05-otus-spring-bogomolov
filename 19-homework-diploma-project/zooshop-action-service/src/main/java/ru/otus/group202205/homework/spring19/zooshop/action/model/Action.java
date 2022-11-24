package ru.otus.group202205.homework.spring19.zooshop.action.model;

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
@Table(name = "actions")
@Getter
@Setter
@EqualsAndHashCode
public class Action {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NonNull
  private String name;
  private String description;
  @NonNull
  @Column(columnDefinition = "TIMESTAMP")
  private LocalDateTime startDate;
  @Column(columnDefinition = "TIMESTAMP")
  private LocalDateTime endDate;
  @NonNull
  @Enumerated(EnumType.STRING)
  private DiscountType discountType;
  @NonNull
  private Float discountValue;
  @NonNull
  private Long goodId;

}
