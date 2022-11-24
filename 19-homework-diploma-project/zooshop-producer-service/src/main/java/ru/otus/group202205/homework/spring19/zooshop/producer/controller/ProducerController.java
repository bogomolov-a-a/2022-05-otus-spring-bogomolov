package ru.otus.group202205.homework.spring19.zooshop.producer.controller;

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
import ru.otus.group202205.homework.spring19.zooshop.producer.dto.ProducerDto;
import ru.otus.group202205.homework.spring19.zooshop.producer.service.ProducerService;

@RestController
@RequiredArgsConstructor
@Validated
public class ProducerController {

  private final ProducerService producerService;

  @Operation(summary = "Create new producer", description = "Address must be already created, otherwise will be 422")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiResponse(responseCode = "201",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = ProducerDto.class))})
  @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public ProducerDto createGood(
      @Parameter(content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"
      )) @RequestBody ProducerDto goodDto) {
    return producerService.save(goodDto);
  }

  @Operation(summary = "Get all producers", description = "Find all producers with pagination.")
  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public List<ProducerDto> findAll(
      @ParameterObject Pageable pageable) {
    return producerService.findAll(org.springframework.data.domain.Pageable
            .ofSize(pageable.getSize())
            .withPage(pageable.getPage()),
        org.springframework.data.domain.Sort.by(Direction.ASC,
            "id"));
  }

  @Operation(summary = "Get producer info by id")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = ProducerDto.class))})
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public ProducerDto findById(@PathVariable("id") Long id) {
    return producerService.findById(id);
  }


  @Operation(summary = "Get producer infos by address id")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = ProducerDto.class))})
  @GetMapping(value = "/by/address", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public List<ProducerDto> findByAddressId(@RequestParam("addressId") Long addressId,
      @ParameterObject Pageable pageable) {
    return producerService.findAllByAddressId(addressId,
        org.springframework.data.domain.Pageable
            .ofSize(pageable.getSize())
            .withPage(pageable.getPage()),
        Sort.by(Direction.ASC,
            "id"));
  }

  @Operation(summary = "Get producer infos by good name")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = ProducerDto.class))})
  @GetMapping(value = "/by/name", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public List<ProducerDto> findByName(@RequestParam("name") String name,
      @ParameterObject Pageable pageable) {
    return producerService.findAllByName(name,
        org.springframework.data.domain.Pageable
            .ofSize(pageable.getSize())
            .withPage(pageable.getPage()),
        Sort.by(Direction.ASC,
            "id"));
  }

  @Operation(summary = "Update producer info")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = ProducerDto.class))})
  @PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public ProducerDto updateGood(@RequestBody ProducerDto goodDto) {
    return producerService.save(goodDto);
  }

  @Operation(summary = "Delete all producers in db")
  @DeleteMapping("/")
  public void deleteAllGoods() {
    producerService.deleteAll();
  }

  @Operation(summary = "Delete producer by id")
  @DeleteMapping("/{id}")
  public void deleteGoodById(@PathVariable("id") Long id) {
    producerService.deleteById(id);
  }

  @Operation(summary = "Delete producer by address id")
  @DeleteMapping("/by/address/{addressId}")
  public void deleteAllGoodsByProducerId(@PathVariable("addressId") Long addressId) {
    producerService.deleteAllByAddressId(addressId);
  }

  @Operation(summary = "Check producer existence by id")
  @GetMapping("/existence/{id}")
  public void existById(@PathVariable("id") Long id) {
    if (!producerService.existsById(id)) {
      throw new NoSuchElementException("entity not found by id " + id);
    }
  }

}
