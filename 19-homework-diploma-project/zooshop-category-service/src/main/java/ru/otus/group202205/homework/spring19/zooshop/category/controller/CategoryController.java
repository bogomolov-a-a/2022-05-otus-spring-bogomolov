package ru.otus.group202205.homework.spring19.zooshop.category.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.group202205.homework.spring19.zooshop.category.dto.CategoryDto;
import ru.otus.group202205.homework.spring19.zooshop.category.service.CategoryService;

@RestController
@RequiredArgsConstructor
@Validated
public class CategoryController {

  private final CategoryService categoryService;

  @Operation(summary = "Create new category", description = "Address must be already created, otherwise will be 422")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiResponse(responseCode = "201",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = CategoryDto.class))})
  @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public CategoryDto createAddress(
      @Parameter(content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"
      )) @RequestBody CategoryDto categoryDto) {
    return categoryService.save(categoryDto);
  }

  @Operation(summary = "Get all categories", description = "Find all categories with pagination.")
  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public List<CategoryDto> findAll(
      @ParameterObject Pageable pageable) {
    return categoryService.findAll(org.springframework.data.domain.Pageable
            .ofSize(pageable.getSize())
            .withPage(pageable.getPage()),
        org.springframework.data.domain.Sort.by(Direction.ASC,
            "id"));
  }

  @Operation(summary = "Get category info by id")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = CategoryDto.class))})
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public CategoryDto findById(@PathVariable("id") Long id) {
    return categoryService.findById(id);
  }


  @Operation(summary = "Get category infos by text", description = "search by all field as 'like'")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = CategoryDto.class))})
  @GetMapping(value = "/by/name", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public List<CategoryDto> findByText(@RequestParam("name") String name,
      @ParameterObject Pageable pageable) {
    return categoryService.findAllByName(name,
        org.springframework.data.domain.Pageable
            .ofSize(pageable.getSize())
            .withPage(pageable.getPage()),
        Sort.by(Direction.ASC,
            "id"));
  }

  @Operation(summary = "Update category info")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = CategoryDto.class))})
  @PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public CategoryDto updateAddress(@RequestBody CategoryDto categoryDto) {
    return categoryService.save(categoryDto);
  }

  @Operation(summary = "Delete all categoryes in db")
  @DeleteMapping("/")
  public void deleteAllAddresses() {
    categoryService.deleteAll();
  }

  @Operation(summary = "Delete category by id")
  @DeleteMapping("/{id}")
  public void deleteAddressById(@PathVariable("id") Long id) {
    categoryService.deleteById(id);
  }

  @Operation(summary = "Check category existence by id")
  @GetMapping("/existence/{id}")
  public void existById(@PathVariable("id") Long id) {
    if (!categoryService.existsById(id)) {
      throw new NoSuchElementException("entity not found by id " + id);
    }
  }

}
