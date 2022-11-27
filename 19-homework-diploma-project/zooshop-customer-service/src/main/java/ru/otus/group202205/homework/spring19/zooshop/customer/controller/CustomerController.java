package ru.otus.group202205.homework.spring19.zooshop.customer.controller;

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
import ru.otus.group202205.homework.spring19.zooshop.customer.dto.CustomerDto;
import ru.otus.group202205.homework.spring19.zooshop.customer.service.CustomerService;

@RestController
@RequiredArgsConstructor
@Validated
public class CustomerController {

  private final CustomerService customerService;

  @Operation(summary = "Create new customer", description = "Address must be already created, otherwise will be 422")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiResponse(responseCode = "201",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = CustomerDto.class))})
  @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public CustomerDto createCustomer(
      @Parameter(content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"
      )) @RequestBody CustomerDto goodDto) {
    return customerService.save(goodDto);
  }

  @Operation(summary = "Get all customers", description = "Find all customers with pagination.")
  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public List<CustomerDto> findAll(
      @ParameterObject Pageable pageable) {
    return customerService.findAll(org.springframework.data.domain.Pageable
            .ofSize(pageable.getSize())
            .withPage(pageable.getPage()),
        org.springframework.data.domain.Sort.by(Direction.ASC,
            "id"));
  }

  @Operation(summary = "Get customer info by id")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = CustomerDto.class))})
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public CustomerDto findById(@PathVariable("id") Long id) {
    return customerService.findById(id);
  }


  @Operation(summary = "Get customer infos by address id")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = CustomerDto.class))})
  @GetMapping(value = "/by/address", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public List<CustomerDto> findByAddressId(@RequestParam("addressId") Long addressId,
      @ParameterObject Pageable pageable) {
    return customerService.findAllByAddressId(addressId,
        org.springframework.data.domain.Pageable
            .ofSize(pageable.getSize())
            .withPage(pageable.getPage()),
        Sort.by(Direction.ASC,
            "id"));
  }

  @Operation(summary = "Update customer info")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = CustomerDto.class))})
  @PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public CustomerDto updateCustomer(@RequestBody CustomerDto goodDto) {
    return customerService.save(goodDto);
  }

  @Operation(summary = "Delete all customers in db")
  @DeleteMapping("/")
  public void deleteAllCustomers() {
    customerService.deleteAll();
  }

  @Operation(summary = "Delete customer by id")
  @DeleteMapping("/{id}")
  public void deleteCustomerById(@PathVariable("id") Long id) {
    customerService.deleteById(id);
  }

  @Operation(summary = "Delete customer by address id")
  @DeleteMapping("/by/address/{addressId}")
  public void deleteAllCustomersByAddressId(@PathVariable("addressId") Long addressId) {
    customerService.deleteAllByAddressId(addressId);
  }

  @Operation(summary = "Check customer existence by id")
  @GetMapping("/existence/{id}")
  public void existById(@PathVariable("id") Long id) {
    if (!customerService.existsById(id)) {
      throw new NoSuchElementException("entity not found by id " + id);
    }
  }

}
