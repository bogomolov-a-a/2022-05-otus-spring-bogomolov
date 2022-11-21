package ru.otus.group202205.homework.spring12.web.rest;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.group202205.homework.spring12.dto.GenreDto;
import ru.otus.group202205.homework.spring12.service.GenreService;

@RestController
@RequiredArgsConstructor
public class GenreController {

  private final GenreService genreService;

  @PostMapping(value = "/genres", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public RedirectView saveGenre(@Valid @ModelAttribute("genre") GenreDto genre) {
    genreService.saveOrUpdate(genre);
    return new RedirectView("/genres/page");
  }

  @GetMapping(value = "/genres", produces = {MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"})
  public List<GenreDto> readGenres() {
    return genreService.findAll();
  }

  @GetMapping(value = "/genres/{id}", produces = {MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"})
  public GenreDto getGenre(@PathVariable(name = "id") Long id) {
    return genreService.findById(id);
  }

  @DeleteMapping(value = "/genres/{id}")
  public void deleteGenre(@PathVariable("id") Long id) {
    genreService.deleteById(id);
  }

}
