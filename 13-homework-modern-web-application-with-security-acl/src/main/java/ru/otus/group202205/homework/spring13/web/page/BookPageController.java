
package ru.otus.group202205.homework.spring13.web.page;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.group202205.homework.spring13.model.Book;
import ru.otus.group202205.homework.spring13.service.AuthorService;
import ru.otus.group202205.homework.spring13.service.GenreService;
import ru.otus.group202205.homework.spring13.web.webutil.ActionMode;
import ru.otus.group202205.homework.spring13.web.webutil.CardService;

@Controller
@RequiredArgsConstructor
public class BookPageController {

  private final CardService cardService;
  private final AuthorService authorService;
  private final GenreService genreService;

  @PreAuthorize("hasRole('ADMIN') or hasRole('READER') and T(ru.otus.group202205.homework.spring13.web.webutil.ActionMode).READ.equals(#actionMode)")
  @GetMapping("/books/{id}/page/{actionMode}")
  public String getBookPage(@PathVariable(value = "id") Long id, @PathVariable(name = "actionMode") ActionMode actionMode, Model model) {
    model.addAttribute("entityClass",
        Book.class.getSimpleName());
    cardService.prepareCard(model,
        actionMode);
    model.addAttribute("id",
        id);
    model.addAttribute("authors",
        authorService.findAll());
    model.addAttribute("genres",
        genreService.findAll());
    return "book";
  }

  @PreAuthorize("hasAnyRole('ADMIN','READER')")
  @GetMapping("/books/page")
  public String getBooksPage() {
    return "books";
  }

}
