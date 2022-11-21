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
import ru.otus.group202205.homework.spring12.dto.BookFullDto;
import ru.otus.group202205.homework.spring12.service.BookService;

@RestController
@RequiredArgsConstructor
public class BookController {

  private final BookService bookService;

  @PostMapping(value = "/books", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public RedirectView saveBook(@Valid @ModelAttribute("book") BookFullDto book) {
    bookService.saveOrUpdate(book);
    return new RedirectView("/books/page");
  }

  @GetMapping(value = "/books", produces = {MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"})
  public List<BookFullDto> readBooks() {
    return bookService.findAll();
  }

  @GetMapping(value = "/books/{id}", produces = {MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"})
  public BookFullDto getBook(@PathVariable(name = "id") Long id) {
    return bookService.findById(id);
  }

  @DeleteMapping(value = "/books/{id}")
  public void deleteBook(@PathVariable("id") Long id) {
    bookService.deleteById(id);
  }

}
