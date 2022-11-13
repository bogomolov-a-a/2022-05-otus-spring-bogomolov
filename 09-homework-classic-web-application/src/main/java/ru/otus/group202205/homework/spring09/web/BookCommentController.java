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
import ru.otus.group202205.homework.spring09.dto.BookCommentDto;
import ru.otus.group202205.homework.spring09.model.BookComment;
import ru.otus.group202205.homework.spring09.service.BookCommentService;
import ru.otus.group202205.homework.spring09.service.BookService;
import ru.otus.group202205.homework.spring09.web.webutil.ActionMode;
import ru.otus.group202205.homework.spring09.web.webutil.CardService;

@Controller
@RequiredArgsConstructor
public class BookCommentController {

  private final BookCommentService bookCommentService;
  private final BookService bookService;
  private final CardService cardService;

  @PostMapping(value = "/book-comments", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ModelAndView saveBookComment(@Valid @ModelAttribute("bookComment") BookCommentDto bookComment, ModelMap model) {
    BookCommentDto savedBookComment = bookCommentService.saveOrUpdate(bookComment);
    model.addAttribute("message",
        String.format("Book comment with id %d saved!",
            savedBookComment.getId()));
    return new ModelAndView("redirect:/book-comments",
        model);
  }

  @GetMapping("/book-comments")
  public String readBookComments(@RequestParam(value = "id", required = false) Long id, Model model) {
    List<BookCommentDto> bookComments = readBookCommentsById(id);
    model.addAttribute("bookComments",
        bookComments);
    return "book-comments";
  }

  @GetMapping("/book-comment")
  public String getBookCommentCardPage(@RequestParam(name = "id", required = false) Long id, @RequestParam(name = "actionMode") ActionMode actionMode,
      Model model, HttpServletResponse response) {
    model.addAttribute("entityClass",
        BookComment.class.getSimpleName());
    cardService.prepareCard(model,
        actionMode);
    Boolean editing = (Boolean) model.getAttribute("editing");
    if (editing != null && editing) {
      putAuthorsAndGenresToModel(model);
    }
    if (ActionMode.CREATE.equals(actionMode)) {
      return "book-comment";
    }
    if (ActionMode.DELETE.equals(actionMode)) {
      model.addAttribute("error",
          "DELETE value 'cardMode' not supported in /book-comments");
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return "error-400";
    }
    if (id != null) {
      putBookCommentDtoInModelIfExists(id,
          model);
      return "book-comment";
    }
    model.addAttribute("error",
        "For read or update request book card id can't be null");

    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    return "error-400";

  }

  @PostMapping(value = "/book-comments/delete", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ModelAndView deleteBookComment(@RequestParam("id") Long id, ModelMap model) {
    bookCommentService.deleteById(id);
    model.addAttribute("message",
        String.format("Book comment with id %d deleted!",
            id));
    return new ModelAndView("redirect:/book-comments",
        model);
  }

  private List<BookCommentDto> readBookCommentsById(Long id) {
    if (id == null) {
      return bookCommentService.findAll();
    }
    return bookCommentService.findAllByBookId(id);

  }

  private void putAuthorsAndGenresToModel(Model model) {
    model.addAttribute("books",
        bookService.findAll());
  }

  private void putBookCommentDtoInModelIfExists(Long id, Model model) {
    BookCommentDto bookComment = bookCommentService.findById(id);
    model.addAttribute("bookComment",
        bookComment);
  }

}
