package ru.otus.group202205.homework.spring13.web.page;

import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.group202205.homework.spring13.model.Author;
import ru.otus.group202205.homework.spring13.web.webutil.ActionMode;
import ru.otus.group202205.homework.spring13.web.webutil.CardService;

@Controller
@RequiredArgsConstructor
public class AuthorPageController {

  private final CardService cardService;

  @PreAuthorize("hasRole('ADMIN') or hasRole('READER') and T(ru.otus.group202205.homework.spring13.web.webutil.ActionMode).READ.equals(#actionMode)")
  @GetMapping("/authors/{id}/page/{actionMode}")
  public String getAuthorPage(@PathVariable(value = "id") Long id, @PathVariable(name = "actionMode") ActionMode actionMode, Model model,
      HttpServletResponse response) {
    model.addAttribute("entityClass",
        Author.class.getSimpleName());
    cardService.prepareCard(model,
        actionMode);
    model.addAttribute("id",
        id);

    return "author";
  }

  @PreAuthorize("hasAnyRole('ADMIN','READER')")
  @GetMapping("/authors/page")
  public String getAuthorsPage() {
    return "authors";
  }

}
