package ru.otus.group202205.homework.spring19.zooshop.action.controller;

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
import ru.otus.group202205.homework.spring19.zooshop.action.dto.ActionDto;
import ru.otus.group202205.homework.spring19.zooshop.action.service.ActionService;

@RestController
@RequiredArgsConstructor
@Validated
public class ActionController {

  private final ActionService actionService;

  @Operation(summary = "Create new action")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiResponse(responseCode = "201",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = ActionDto.class))})
  @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public ActionDto createAction(
      @Parameter(content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"
      )) @RequestBody ActionDto actionDto) {
    return actionService.save(actionDto);
  }

  @Operation(summary = "Get all actions", description = "Find all actions with pagination.")
  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public List<ActionDto> findAll(
      @ParameterObject Pageable pageable) {
    return actionService.findAll(org.springframework.data.domain.Pageable
            .ofSize(pageable.getSize())
            .withPage(pageable.getPage()),
        org.springframework.data.domain.Sort.by(Direction.ASC,
            "id"));
  }

  @Operation(summary = "Get action info by id")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = ActionDto.class))})
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public ActionDto findById(@PathVariable("id") Long id) {
    return actionService.findById(id);
  }


  @Operation(summary = "Get action infos by name")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = ActionDto.class))})
  @GetMapping(value = "/by/name", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public List<ActionDto> findByName(@RequestParam("name") String name,
      @ParameterObject Pageable pageable) {
    return actionService.findAllByName(name,
        org.springframework.data.domain.Pageable
            .ofSize(pageable.getSize())
            .withPage(pageable.getPage()),
        Sort.by(Direction.ASC,
            "id"));
  }

  @Operation(summary = "Update action info")
  @ApiResponse(responseCode = "200",
      content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
          schema = @Schema(implementation = ActionDto.class))})
  @PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
  public ActionDto updateAction(@RequestBody ActionDto actionDto) {
    return actionService.save(actionDto);
  }

  @Operation(summary = "Delete all actiones in db")
  @DeleteMapping("/")
  public void deleteAllActiones() {
    actionService.deleteAll();
  }

  @Operation(summary = "Delete action by id")
  @DeleteMapping("/{id}")
  public void deleteActionById(@PathVariable("id") Long id) {
    actionService.deleteById(id);
  }

  @Operation(summary = "Check action existence by id")
  @GetMapping("/existence/{id}")
  public void existById(@PathVariable("id") Long id) {
    if (!actionService.existsById(id)) {
      throw new NoSuchElementException("entity not found by id " + id);
    }
  }

}
