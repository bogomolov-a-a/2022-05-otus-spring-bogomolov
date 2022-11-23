package ru.otus.group202205.homework.spring19.zooshop.good.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
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
import ru.otus.group202205.homework.spring19.zooshop.good.dto.GoodDto;
import ru.otus.group202205.homework.spring19.zooshop.good.service.GoodService;

@RestController
@RequiredArgsConstructor
@Validated
public class GoodController {

  private final GoodService goodService;

  @Operation(summary = "Create new good", description = "Producer and category must be already created, otherwise will be 422")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiResponse(responseCode = "201",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = GoodDto.class))})
  @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public GoodDto createGood(
      @Parameter(content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"
      )) @RequestBody GoodDto goodDto) {
    return goodService.save(goodDto);
  }

  @Operation(summary = "Get all goods", description = "Find all goods with pagination.")
  @GetMapping(value = "/page/{number}/per/{size}/sort/{sort}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public List<GoodDto> findAll(
      @ParameterObject Pageable pageable) {
    return goodService.findAll(org.springframework.data.domain.Pageable
            .ofSize(
                pageable.getSize()
            )
            .withPage(pageable.getPage()),
        org.springframework.data.domain.Sort.by(Direction.ASC,
            "id"));
  }

  @Operation(summary = "Get good info by id")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = GoodDto.class))})
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public GoodDto findById(@PathVariable("id") Long id) {
    return goodService.findById(id);
  }

  @Operation(summary = "Get good infos by category id")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = GoodDto.class))})
  @GetMapping(value = "/page/{number}/per/{size}/sort/{sort}/by/category", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public List<GoodDto> findByCategoryId(@RequestParam("categoryId") Long categoryId,
      @ParameterObject Pageable pageable) {
    return goodService.findAllByCategoryId(categoryId,
        org.springframework.data.domain.Pageable
            .ofSize(pageable.getSize())
            .withPage(pageable.getPage()),
        Sort.by(Direction.ASC,
            "id"));
  }

  @Operation(summary = "Get good infos by producer id")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = GoodDto.class))})
  @GetMapping(value = "/page/{number}/per/{size}/sort/{sort}/by/producer", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public List<GoodDto> findByProducerId(@RequestParam("producerId") Long producerId,
      @ParameterObject Pageable pageable) {
    return goodService.findAllByProducerId(producerId,
        org.springframework.data.domain.Pageable
            .ofSize(pageable.getSize())
            .withPage(pageable.getPage()),
        Sort.by(Direction.ASC,
            "id"));
  }

  @Operation(summary = "Get good infos by good name")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = GoodDto.class))})
  @GetMapping(value = "/page/{number}/per/{size}/sort/{sort}/by/name", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public List<GoodDto> findByName(@RequestParam("name") String name,
      @ParameterObject Pageable pageable) {
    return goodService.findAllByName(name,
        org.springframework.data.domain.Pageable
            .ofSize(pageable.getSize())
            .withPage(pageable.getPage()),
        Sort.by(Direction.ASC,
            "id"));
  }

  @Operation(summary = "Get good info by price between start and end price")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = GoodDto.class))})
  @GetMapping(value = "/page/{number}/per/{size}/sort/{sort}/by/price", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public List<GoodDto> findByPrice(@RequestParam("startPrice") Float startPrice,
      @RequestParam("endPrice") Float endPrice,
      @ParameterObject Pageable pageable) {
    return goodService.findAllByPriceBetween(startPrice,
        endPrice,
        org.springframework.data.domain.Pageable
            .ofSize(pageable.getSize())
            .withPage(pageable.getPage()),
        Sort.by(Direction.ASC,
            "id"));
  }

  @Operation(summary = "Update good info")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = GoodDto.class))})
  @PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public GoodDto updateGood(@RequestBody GoodDto goodDto) {
    return goodService.save(goodDto);
  }

  @Operation(summary = "Delete all goods in db")
  @DeleteMapping("/")
  public void deleteAllGoods() {
    goodService.deleteAll();
  }

  @Operation(summary = "Delete good by id")
  @DeleteMapping("/{id}")
  public void deleteGoodById(@PathVariable("id") Long id) {
    goodService.deleteById(id);
  }

  @Operation(summary = "Delete good by producer id")
  @DeleteMapping("/by/producer/{producerId}")
  public void deleteAllGoodsByProducerId(@PathVariable("producerId") Long producerId) {
    goodService.deleteAllByProducerId(producerId);
  }

  @Operation(summary = "Delete good by category id")
  @DeleteMapping("/by/category/{categoryId}")
  public void deleteAllGoodsByCategoryId(@PathVariable("categoryId") Long categoryId) {
    goodService.deleteAllByCategoryId(categoryId);
  }

}
