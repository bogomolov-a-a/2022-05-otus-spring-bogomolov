package ru.otus.group202205.homework.spring10.web.page;

import static org.mockito.ArgumentMatchers.any;

import java.io.File;
import java.nio.file.Files;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import ru.otus.group202205.homework.spring10.web.webutil.ActionMode;
import ru.otus.group202205.homework.spring10.web.webutil.CardService;

@WebMvcTest(AuthorPageController.class)
class AuthorPageControllerTest {

  @Autowired
  private MockMvc mvc;
  @MockBean
  private CardService cardService;

  @BeforeEach
  void init() {
    Mockito.reset(cardService);
    Mockito
        .doAnswer(invocation -> {
          Model model = invocation.getArgument(0);
          ActionMode actionMode = invocation.getArgument(1);
          String entityName = ((String) model.getAttribute("entityClass")).toLowerCase(Locale.ROOT);
          switch (actionMode) {
            case READ: {
              model.addAttribute("pageTitle",
                  String.format("Read %s page",
                      entityName));
              break;
            }
            case CREATE: {
              model.addAttribute("pageTitle",
                  String.format("Add new %s",
                      entityName));
              break;
            }
            case UPDATE: {
              model.addAttribute("pageTitle",
                  String.format("Update %s page",
                      entityName));
            }
          }
          boolean isView = ActionMode.READ.equals(actionMode);
          boolean isUpdating = ActionMode.UPDATE.equals(actionMode);
          boolean isCreating = ActionMode.CREATE.equals(actionMode);
          model.addAttribute("viewing",
              isView);
          model.addAttribute("editing",
              isCreating || isUpdating);
          model.addAttribute("updating",
              isUpdating);
          if (isUpdating) {
            model.addAttribute("submitButtonCaption",
                "Update");
            return null;
          }
          boolean editing = Boolean.parseBoolean(String.valueOf(model.getAttribute("editing")));
          if (editing) {
            model.addAttribute("submitButtonCaption",
                "Create");
          }
          return null;
        })
        .when(cardService)
        .prepareCard(any(),
            any());
  }

  @Test
  void shouldBeReturnAuthorCreateCardPage() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/author/get-author/card-to-create.html",
        AuthorPageControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders.get("/authors/0/page/CREATE"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isOk())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(exceptedContent));
  }

  @Test
  void shouldBeReturnCode400AuthorReadCardPageWrongId() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/author/get-author/read-update-card-id-error.html",
        AuthorPageControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders.get("/authors/null/page/READ"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isBadRequest())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("author",
                "entityClass"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists(("error")))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(exceptedContent));
  }

  @Test
  void shouldBeReturnCode400AuthorUpdateCardPageWithoutId() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/author/get-author/read-update-card-id-error.html",
        AuthorPageControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders.get("/authors/null/page/UPDATE"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isBadRequest())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("author",
                "entityClass"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("error"))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(exceptedContent));
  }

  @Test
  void shouldBeReturnCode200AuthorReadCardPageWithId() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/author/get-author/read-author-200-id-1-read.html",
        AuthorPageControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders.get("/authors/1/page/READ"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isOk())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("pageTitle",
                "Read author page"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("author",
                "error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("entityClass"))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(exceptedContent));
  }

  @Test
  void shouldBeReturnCode200ReadAuthors() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/author/get-author/read-authors-200.html",
        AuthorPageControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders.get("/authors/page"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isOk())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("authors",
                "error",
                "entityClass"))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(exceptedContent));
  }

}