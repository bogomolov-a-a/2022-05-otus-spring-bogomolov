package ru.otus.group202205.homework.spring10.web.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.group202205.homework.spring10.model.BookComment;
import ru.otus.group202205.homework.spring10.service.BookService;
import ru.otus.group202205.homework.spring10.web.webutil.ActionMode;
import ru.otus.group202205.homework.spring10.web.webutil.CardService;

@Controller
@RequiredArgsConstructor
public class BookCommentPageController {

  private final BookService bookService;
  private final CardService cardService;

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

  @GetMapping("/book-comments/page")
  public String getBookCommentsPage() {
    return "book-comments";
  }

}
