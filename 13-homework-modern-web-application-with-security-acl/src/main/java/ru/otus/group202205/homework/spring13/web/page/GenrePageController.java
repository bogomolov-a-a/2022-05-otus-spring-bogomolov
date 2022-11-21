
package ru.otus.group202205.homework.spring13.web.page;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.group202205.homework.spring13.model.Genre;
import ru.otus.group202205.homework.spring13.web.webutil.ActionMode;
import ru.otus.group202205.homework.spring13.web.webutil.CardService;

@Controller
@RequiredArgsConstructor
public class GenrePageController {

  private final CardService cardService;

  @PreAuthorize("hasRole('ADMIN') or hasRole('READER') and T(ru.otus.group202205.homework.spring13.web.webutil.ActionMode).READ.equals(#actionMode)")
  @GetMapping("/genres/{id}/page/{actionMode}")
  public String getGenrePage(@PathVariable(value = "id") Long id, @PathVariable(name = "actionMode") ActionMode actionMode, Model model) {
    model.addAttribute("entityClass",
        Genre.class.getSimpleName());
    cardService.prepareCard(model,
        actionMode);
    model.addAttribute("id",
        id);

    return "genre";
  }

  @PreAuthorize("hasAnyRole('ADMIN','READER')")
  @GetMapping("/genres/page")
  public String getGenresPage() {
    return "genres";
  }

}
