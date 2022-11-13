package ru.otus.group202205.homework.spring09.web;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.group202205.homework.spring09.dto.BookFullDto;
import ru.otus.group202205.homework.spring09.model.Book;
import ru.otus.group202205.homework.spring09.service.AuthorService;
import ru.otus.group202205.homework.spring09.service.BookService;
import ru.otus.group202205.homework.spring09.service.GenreService;
import ru.otus.group202205.homework.spring09.web.webutil.ActionMode;
import ru.otus.group202205.homework.spring09.web.webutil.CardService;

@Controller
@RequiredArgsConstructor
public class BookController {

  private final BookService bookService;
  private final AuthorService authorService;
  private final GenreService genreService;

  private final CardService cardService;

  @PostMapping(value = "/books", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ModelAndView saveBook(@Valid @ModelAttribute("book") BookFullDto book, ModelMap model) {
    BookFullDto savedBook = bookService.saveOrUpdate(book);
    model.addAttribute("message",
        String.format("Book with id %d saved!",
            savedBook.getId()));
    return new ModelAndView("redirect:/books",
        model);
  }

  @GetMapping("/books")
  public String readBooks(Model model) {
    List<BookFullDto> books = bookService.findAll();
    model.addAttribute("books",
        books);
    return "books";
  }

  @GetMapping("/book")
  public String getBookCardPage(@RequestParam(name = "id", required = false) Long id, @RequestParam(name = "actionMode") ActionMode actionMode, Model model,
      HttpServletResponse response) {
    model.addAttribute("entityClass",
        Book.class.getSimpleName());
    cardService.prepareCard(model,
        actionMode);
    Boolean editing = (Boolean) model.getAttribute("editing");
    if (editing != null && editing) {
      putAuthorsAndGenresToModel(model);
    }
    if (ActionMode.CREATE.equals(actionMode)) {
      return "book";
    }
    if (ActionMode.DELETE.equals(actionMode)) {
      model.addAttribute("error",
          "DELETE value 'cardMode' not supported in /book");
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return "error-400";
    }
    if (id != null) {
      putBookFullDtoInModelIfExists(id,
          model);
      return "book";
    }
    model.addAttribute("error",
        "For read or update request book card id can't be null");

    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    return "error-400";

  }

  @PostMapping(value = "/books/delete", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ModelAndView deleteBook(@RequestParam("id") Long id, ModelMap model) {
    bookService.deleteById(id);
    model.addAttribute("message",
        String.format("Book with id %d deleted!",
            id));
    return new ModelAndView("redirect:/books",
        model);
  }

  private void putAuthorsAndGenresToModel(Model model) {
    model.addAttribute("authors",
        authorService.findAll());
    model.addAttribute("genres",
        genreService.findAll());
  }

  private void putBookFullDtoInModelIfExists(Long id, Model model) {
    BookFullDto book = bookService.findById(id);
    model.addAttribute("book",
        book);
  }

}
