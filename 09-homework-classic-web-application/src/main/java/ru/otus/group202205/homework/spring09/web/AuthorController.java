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
import ru.otus.group202205.homework.spring09.dto.AuthorDto;
import ru.otus.group202205.homework.spring09.model.Author;
import ru.otus.group202205.homework.spring09.service.AuthorService;
import ru.otus.group202205.homework.spring09.web.webutil.ActionMode;
import ru.otus.group202205.homework.spring09.web.webutil.CardService;

@Controller
@RequiredArgsConstructor
public class AuthorController {

  private final CardService cardService;
  private final AuthorService authorService;

  @PostMapping(value = "/authors", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ModelAndView saveAuthor(@Valid @ModelAttribute("author") AuthorDto author, ModelMap model) {
    AuthorDto savedAuthor = authorService.saveOrUpdate(author);
    model.addAttribute("message",
        String.format("Author with id %d saved!",
            savedAuthor.getId()));
    return new ModelAndView("redirect:/authors",
        model);
  }

  @GetMapping("/authors")
  public String readAuthors(Model model) {
    List<AuthorDto> authors = authorService.findAll();
    model.addAttribute("authors",
        authors);
    return "authors";
  }

  @GetMapping("/author")
  public String getAuthorCardPage(@RequestParam(name = "id", required = false) Long id, @RequestParam(name = "actionMode") ActionMode actionMode, Model model,
      HttpServletResponse response) {
    model.addAttribute("entityClass",
        Author.class.getSimpleName());
    cardService.prepareCard(model,
        actionMode);
    if (ActionMode.CREATE.equals(actionMode)) {
      return "author";
    }
    if (ActionMode.DELETE.equals(actionMode)) {
      model.addAttribute("error",
          "DELETE value 'cardMode' not supported in /author");
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return "error-400";
    }
    if (id != null) {
      putAuthorDtoInModelIfExists(id,
          model);
      return "author";
    }
    model.addAttribute("error",
        "For read or update request author card id can't be null");

    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    return "error-400";

  }

  @PostMapping(value = "/authors/delete", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ModelAndView deleteAuthor(@RequestParam("id") Long id, ModelMap model) {
    authorService.deleteById(id);
    model.addAttribute("message",
        String.format("Author with id %d deleted!",
            id));
    return new ModelAndView("redirect:/authors",
        model);
  }

  private void putAuthorDtoInModelIfExists(Long id, Model model) {
    AuthorDto author = authorService.findById(id);
    model.addAttribute("author",
        author);
  }

}
