package ru.otus.group202205.homework.spring19.zooshop.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
@EqualsAndHashCode
@Schema(title = "Category information")
public class CategoryDto {

  private Long id;
  @NonNull
  private String name;
  private Long parentId;

}
