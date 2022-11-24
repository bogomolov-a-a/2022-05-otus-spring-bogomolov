package ru.otus.group202205.homework.spring13.web.page;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.group202205.homework.spring13.model.BookComment;
import ru.otus.group202205.homework.spring13.service.BookService;
import ru.otus.group202205.homework.spring13.web.webutil.ActionMode;
import ru.otus.group202205.homework.spring13.web.webutil.CardService;

@Controller
@RequiredArgsConstructor
public class BookCommentPageController {

  private final BookService bookService;
  private final CardService cardService;

  @PreAuthorize("hasRole('ADMIN') or hasRole('READER') and T(ru.otus.group202205.homework.spring13.web.webutil.ActionMode).READ.equals(#actionMode)")
  @GetMapping("/book-comments/{id}/page/{actionMode}")
  public String getBookCommentPage(@PathVariable(value = "id") Long id, @PathVariable(name = "actionMode") ActionMode actionMode, Model model) {
    model.addAttribute("entityClass",
        BookComment.class.getSimpleName());
    cardService.prepareCard(model,
        actionMode);
    model.addAttribute("id",
        id);
    model.addAttribute("books",
        bookService.findAll());
    return "book-comment";
  }

  @PreAuthorize("hasAnyRole('ADMIN','READER')")
  @GetMapping("/book-comments/page")
  public String getBookCommentsPage() {
    return "book-comments";
  }

}
