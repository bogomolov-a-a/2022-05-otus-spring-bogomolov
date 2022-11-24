package ru.otus.group202205.homework.spring12.web.rest;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.group202205.homework.spring12.dto.AuthorDto;
import ru.otus.group202205.homework.spring12.service.AuthorService;

@RestController
@RequiredArgsConstructor
public class AuthorController {

  private final AuthorService authorService;

  @Validated
  @PostMapping(value = "/authors", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public RedirectView saveAuthor(@Valid AuthorDto author) {
    authorService.saveOrUpdate(author);
    return new RedirectView("/authors/page");
  }

  @GetMapping(value = "/authors", produces = {MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"})
  public List<AuthorDto> readAuthors() {
    return authorService.findAll();
  }

  @GetMapping(value = "/authors/{id}", produces = {MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"})
  public AuthorDto getAuthor(@PathVariable(name = "id") Long id) {
    return authorService.findById(id);
  }

  @DeleteMapping(value = "/authors/{id}")
  public void deleteAuthor(@PathVariable("id") Long id) {
    authorService.deleteById(id);
  }

}
