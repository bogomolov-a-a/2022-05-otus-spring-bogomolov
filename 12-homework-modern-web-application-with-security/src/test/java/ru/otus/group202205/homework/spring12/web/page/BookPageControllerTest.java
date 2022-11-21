package ru.otus.group202205.homework.spring12.web.page;

import static org.mockito.ArgumentMatchers.any;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import ru.otus.group202205.homework.spring12.dto.AuthorDto;
import ru.otus.group202205.homework.spring12.dto.GenreDto;
import ru.otus.group202205.homework.spring12.model.Author;
import ru.otus.group202205.homework.spring12.model.Genre;
import ru.otus.group202205.homework.spring12.service.AuthorService;
import ru.otus.group202205.homework.spring12.service.GenreService;
import ru.otus.group202205.homework.spring12.service.mapper.AuthorMapper;
import ru.otus.group202205.homework.spring12.service.mapper.GenreMapper;
import ru.otus.group202205.homework.spring12.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring12.testdata.GenreTestDataComponent;
import ru.otus.group202205.homework.spring12.web.webutil.ActionMode;
import ru.otus.group202205.homework.spring12.web.webutil.CardService;

@WebMvcTest(BookPageController.class)
@Import({AuthorTestDataComponent.class, GenreTestDataComponent.class})
class BookPageControllerTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;
  @Autowired
  private GenreTestDataComponent genreTestDataComponent;
  @MockBean
  private CardService cardService;
  @MockBean
  private AuthorService authorService;
  @MockBean
  private GenreService genreService;
  @MockBean
  private AuthorMapper authorMapper;
  @MockBean
  private GenreMapper genreMapper;

  @BeforeEach
  void init() {
    Mockito.reset(cardService);
    Mockito.reset(authorService);
    Mockito.reset(genreService);
    Mockito.reset(authorMapper);
    Mockito.reset(genreMapper);
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
    Mockito
        .doAnswer(invocation -> {
          Author author = invocation.getArgument(0);
          AuthorDto result = new AuthorDto();
          result.setId(author.getId());
          result.setName(author.getName());
          result.setSurname(author.getSurname());
          result.setPatronymic(author.getPatronymic());
          result.setBirthYear(author.getBirthYear());
          result.setDeathYear(author.getDeathYear());
          return result;
        })
        .when(authorMapper)
        .toDto(any());
    List<AuthorDto> authorDtos = authorTestDataComponent
        .getAllExistingAuthors()
        .stream()
        .map(authorMapper::toDto)
        .collect(Collectors.toList());
    Mockito
        .doReturn(authorDtos)
        .when(authorService)
        .findAll();

    Mockito
        .doAnswer(invocation -> {
          Genre genre = invocation.getArgument(0);
          GenreDto result = new GenreDto();
          result.setId(genre.getId());
          result.setName(genre.getName());
          return result;
        })
        .when(genreMapper)
        .toDto(any());
    Mockito
        .doAnswer(invocation -> {
          GenreDto genre = invocation.getArgument(0);
          Genre result = new Genre();
          result.setId(genre.getId());
          result.setName(genre.getName());
          return result;
        })
        .when(genreMapper)
        .toEntity(any());
    Mockito
        .doAnswer(invocation -> {
          AuthorDto authorDto = invocation.getArgument(0);
          Author result = new Author();
          result.setId(authorDto.getId());
          result.setName(authorDto.getName());
          result.setSurname(authorDto.getSurname());
          result.setPatronymic(authorDto.getPatronymic());
          result.setBirthYear(authorDto.getBirthYear());
          result.setDeathYear(authorDto.getDeathYear());
          return result;
        })
        .when(authorMapper)
        .toEntity(any());
    List<GenreDto> genreDtos = genreTestDataComponent
        .getAllExistingGenres()
        .stream()
        .map(genreMapper::toDto)
        .collect(Collectors.toList());
    Mockito
        .doAnswer(invocation ->
            genreDtos
        )
        .when(genreService)
        .findAll();
  }

  @WithMockUser(username = "admin", password = "admin")
  @Test
  void shouldBeReturnBookCreateCardPage() throws
      Exception {
    mvc
        .perform(MockMvcRequestBuilders
            .get("/books/0/page/CREATE"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isOk())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"));
  }

  @WithMockUser(username = "admin", password = "admin")
  @Test
  void shouldBeReturnCode400BookReadCardPageWrongId() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/book/get-book/read-update-card-id-error.html",
        BookPageControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders.get("/books/null/page/READ"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isBadRequest())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("book",
                "entityClass"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists(("error")))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(exceptedContent));
  }

  @WithMockUser(username = "admin", password = "admin")
  @Test
  void shouldBeReturnCode400BookUpdateCardPageWithoutId() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/book/get-book/read-update-card-id-error.html",
        BookPageControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders.get("/books/null/page/UPDATE"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isBadRequest())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("book",
                "entityClass"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("error"))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(exceptedContent));
  }

  @WithMockUser(username = "admin", password = "admin")
  @Test
  void shouldBeReturnCode200BookReadCardPageWithId() throws
      Exception {
    mvc
        .perform(MockMvcRequestBuilders.get("/books/1/page/READ"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isOk())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("pageTitle",
                "Read book page"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("book",
                "error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("entityClass"));
  }

  @WithMockUser(username = "admin", password = "admin")
  @Test
  void shouldBeReturnCode200ReadBooks() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/book/get-book/read-books-200.html",
        BookPageControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders.get("/books/page"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isOk())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("books",
                "error",
                "entityClass"))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(exceptedContent));
  }

  @Test
  void shouldBeReturn401withoutUserBookPageUpdate() throws
      Exception {
    mvc
        .perform(MockMvcRequestBuilders.get("/books/1/page/READ"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isUnauthorized());
  }

  @Test
  void shouldBeReturn401withoutUserBookPageRead() throws
      Exception {
    mvc
        .perform(MockMvcRequestBuilders.get("/books/1/page/UPDATE"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isUnauthorized());
  }

  @Test
  void shouldBeReturn401withoutUserBookPageCreate() throws
      Exception {
    mvc
        .perform(MockMvcRequestBuilders.get("/books/0/page/CREATE"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isUnauthorized());
  }

  @Test
  void shouldBeReturn401withoutUserBooksPage() throws
      Exception {
    mvc
        .perform(MockMvcRequestBuilders.get("/books/page"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isUnauthorized());
  }

}