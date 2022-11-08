package ru.otus.group202205.homework.spring19.zooshop.good.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.group202205.homework.spring19.zooshop.good.dto.GoodDto;
import ru.otus.group202205.homework.spring19.zooshop.good.service.GoodService;

@RestController
@RequiredArgsConstructor
@Validated
public class GoodController {

  private final GoodService goodService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/goods/")
  public GoodDto createGood(GoodDto goodDto) {
    return goodService.save(goodDto);
  }

  @GetMapping("/goods/")
  public List<GoodDto> findAll() {
    return null;//return goodService.findAll()
  }

}
