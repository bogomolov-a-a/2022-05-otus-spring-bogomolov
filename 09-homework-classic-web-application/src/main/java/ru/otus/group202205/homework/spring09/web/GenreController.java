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
import ru.otus.group202205.homework.spring09.dto.GenreDto;
import ru.otus.group202205.homework.spring09.model.Genre;
import ru.otus.group202205.homework.spring09.service.GenreService;
import ru.otus.group202205.homework.spring09.web.webutil.ActionMode;
import ru.otus.group202205.homework.spring09.web.webutil.CardService;

@Controller
@RequiredArgsConstructor
public class GenreController {

  private final CardService cardService;
  private final GenreService genreService;

  @PostMapping(value = "/genres", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ModelAndView saveGenre(@Valid @ModelAttribute("genre") GenreDto genre, ModelMap model) {
    GenreDto savedAuthor = genreService.saveOrUpdate(genre);
    model.addAttribute("message",
        String.format("Genre with id %d saved!",
            savedAuthor.getId()));
    return new ModelAndView("redirect:/genres",
        model);
  }

  @GetMapping("/genres")
  public String readGenres(Model model) {
    List<GenreDto> genres = genreService.findAll();
    model.addAttribute("genres",
        genres);
    return "genres";
  }

  @GetMapping("/genre")
  public String getGenreCardPage(@RequestParam(name = "id", required = false) Long id, @RequestParam(name = "actionMode") ActionMode actionMode, Model model,
      HttpServletResponse response) {
    model.addAttribute("entityClass",
        Genre.class.getSimpleName());
    cardService.prepareCard(model,
        actionMode);
    if (ActionMode.CREATE.equals(actionMode)) {
      return "genre";
    }
    if (ActionMode.DELETE.equals(actionMode)) {
      model.addAttribute("error",
          "DELETE value 'cardMode' not supported in /genre");
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return "error-400";
    }
    if (id != null) {
      putGenreDtoInModelIfExists(id,
          model);
      return "genre";
    }
    model.addAttribute("error",
        "For read or update request genre card id can't be null");
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    return "error-400";

  }

  @PostMapping(value = "/genres/delete", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ModelAndView deleteGenre(@RequestParam("id") Long id, ModelMap model) {
    genreService.deleteById(id);
    model.addAttribute("message",
        String.format("Genre with id %d deleted!",
            id));
    return new ModelAndView("redirect:/genres",
        model);
  }

  private void putGenreDtoInModelIfExists(Long id, Model model) {
    GenreDto genre = genreService.findById(id);
    model.addAttribute("genre",
        genre);
  }

}
