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
import ru.otus.group202205.homework.spring12.dto.BookCommentDto;
import ru.otus.group202205.homework.spring12.service.BookCommentService;

@RestController
@RequiredArgsConstructor
public class BookCommentController {

  private final BookCommentService bookCommentService;

  @PostMapping(value = "/book-comments", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public RedirectView saveBookComment(@Valid @ModelAttribute("bookComment") BookCommentDto bookComment) {
    bookCommentService.saveOrUpdate(bookComment);
    return new RedirectView("/book-comments/page");
  }

  @GetMapping(value = "/book-comments", produces = {MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"})
  public List<BookCommentDto> readBookComments() {
    return bookCommentService.findAll();
  }

  @GetMapping(value = "/book-comments/{id}", produces = {MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"})
  public BookCommentDto getBookComment(@PathVariable(name = "id", required = false) Long id) {
    return bookCommentService.findById(id);
  }

  @DeleteMapping(value = "/book-comments/{id}")
  public void deleteBookComment(@PathVariable("id") Long id) {
    bookCommentService.deleteById(id);
  }

}
