package ru.otus.group202205.homework.spring19.zooshop.order.controller;

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
import ru.otus.group202205.homework.spring19.zooshop.order.dto.OrderDto;
import ru.otus.group202205.homework.spring19.zooshop.order.service.OrderService;

@RestController
@RequiredArgsConstructor
@Validated
public class OrderController {

  private final OrderService orderService;

  @Operation(summary = "Create new order")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiResponse(responseCode = "201",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = OrderDto.class))})
  @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public OrderDto createOrder(
      @Parameter(content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"
      )) @RequestBody OrderDto orderDto) {
    return orderService.save(orderDto);
  }

  @Operation(summary = "Get all orders", description = "Find all orders with pagination.")
  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public List<OrderDto> findAll(
      @ParameterObject Pageable pageable) {
    return orderService.findAll(org.springframework.data.domain.Pageable
            .ofSize(pageable.getSize())
            .withPage(pageable.getPage()),
        org.springframework.data.domain.Sort.by(Direction.ASC,
            "id"));
  }

  @Operation(summary = "Get order info by id")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = OrderDto.class))})
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public OrderDto findById(@PathVariable("id") Long id) {
    return orderService.findById(id);
  }


  @Operation(summary = "Get order infos by customer id")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = OrderDto.class))})
  @GetMapping(value = "/by/customer", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public List<OrderDto> findByCustomerId(@RequestParam("customerId") long customerId,
      @ParameterObject Pageable pageable) {
    return orderService.findAllByCustomerId(customerId,
        org.springframework.data.domain.Pageable
            .ofSize(pageable.getSize())
            .withPage(pageable.getPage()),
        Sort.by(Direction.ASC,
            "id"));
  }

  @Operation(summary = "Update order info")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = OrderDto.class))})
  @PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public OrderDto updateOrder(@RequestBody OrderDto orderDto) {
    return orderService.save(orderDto);
  }

  @Operation(summary = "Delete all orders in db")
  @DeleteMapping("/")
  public void deleteAllOrders() {
    orderService.deleteAll();
  }

  @Operation(summary = "Delete order by id")
  @DeleteMapping("/{id}")
  public void deleteOrderById(@PathVariable("id") Long id) {
    orderService.deleteById(id);
  }

  @Operation(summary = "Delete order by customer id")
  @DeleteMapping("/by/customer/{customerId}")
  public void deleteAllByCustomerId(@PathVariable("customerId") Long customerId) {
    orderService.deleteAllByCustomerId(customerId);
  }

  @Operation(summary = "Delete orders by address id")
  @DeleteMapping("/by/address/{addressId}")
  public void deleteAllByAddressId(@PathVariable("addressId") Long addressId) {
    orderService.deleteAllByAddressId(addressId);
  }

  @Operation(summary = "Check order existence by id")
  @GetMapping("/existence/{id}")
  public void existById(@PathVariable("id") Long id) {
    if (!orderService.existsById(id)) {
      throw new NoSuchElementException("entity not found by id " + id);
    }
  }

}
