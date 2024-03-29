package ru.otus.group202205.homework.spring19.zooshop.orderposition.controller;

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
import ru.otus.group202205.homework.spring19.zooshop.orderposition.dto.OrderPositionDto;
import ru.otus.group202205.homework.spring19.zooshop.orderposition.service.OrderPositionService;

@RestController
@RequiredArgsConstructor
@Validated
public class OrderPositionController {

  private final OrderPositionService orderPositionService;

  @Operation(summary = "Create new order position")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiResponse(responseCode = "201",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = OrderPositionDto.class))})
  @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public OrderPositionDto createOrderPosition(
      @Parameter(content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"
      )) @RequestBody OrderPositionDto orderPositionDto) {
    return orderPositionService.save(orderPositionDto);
  }

  @Operation(summary = "Get all order positions", description = "Find all order positions with pagination.")
  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public List<OrderPositionDto> findAll(
      @ParameterObject Pageable pageable) {
    return orderPositionService.findAll(org.springframework.data.domain.Pageable
            .ofSize(pageable.getSize())
            .withPage(pageable.getPage()),
        org.springframework.data.domain.Sort.by(Direction.ASC,
            "id"));
  }

  @Operation(summary = "Get order position info by id")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = OrderPositionDto.class))})
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public OrderPositionDto findById(@PathVariable("id") Long id) {
    return orderPositionService.findById(id);
  }


  @Operation(summary = "Get order position infos by name")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = OrderPositionDto.class))})
  @GetMapping(value = "/by/name", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public List<OrderPositionDto> findByOrderId(@RequestParam("orderId") long orderId,
      @ParameterObject Pageable pageable) {
    return orderPositionService.findAllByOrderId(orderId,
        org.springframework.data.domain.Pageable
            .ofSize(pageable.getSize())
            .withPage(pageable.getPage()),
        Sort.by(Direction.ASC,
            "id"));
  }

  @Operation(summary = "Update order position info")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = OrderPositionDto.class))})
  @PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public OrderPositionDto updateOrderPosition(@RequestBody OrderPositionDto orderPositionDto) {
    return orderPositionService.save(orderPositionDto);
  }

  @Operation(summary = "Delete all order positions in db")
  @DeleteMapping("/")
  public void deleteAllOrderPositions() {
    orderPositionService.deleteAll();
  }

  @Operation(summary = "Delete order position by id")
  @DeleteMapping("/{id}")
  public void deleteOrderPositionById(@PathVariable("id") Long id) {
    orderPositionService.deleteById(id);
  }

  @Operation(summary = "Delete order position by good id")
  @DeleteMapping("/by/good/{goodId}")
  public void deleteAllByGoodId(@PathVariable("goodId") Long goodId) {
    orderPositionService.deleteAllByGoodId(goodId);
  }

  @Operation(summary = "Delete order position by action id")
  @DeleteMapping("/by/action/{actionId}")
  public void deleteAllByActionId(@PathVariable("actionId") Long actionId) {
    orderPositionService.deleteAllByActionId(actionId);
  }

  @Operation(summary = "Delete order position by action id")
  @DeleteMapping("/by/order/{actionId}")
  public void deleteAllByOrderId(@PathVariable("orderId") Long orderId) {
    orderPositionService.deleteAllByOrderId(orderId);
  }

  @Operation(summary = "Check order position existence by id")
  @GetMapping("/existence/{id}")
  public void existById(@PathVariable("id") Long id) {
    if (!orderPositionService.existsById(id)) {
      throw new NoSuchElementException("entity not found by id " + id);
    }
  }

}
