package ru.otus.group202205.homework.spring09.web;

import static org.mockito.ArgumentMatchers.any;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import ru.otus.group202205.homework.spring09.dto.AuthorDto;
import ru.otus.group202205.homework.spring09.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring09.model.Author;
import ru.otus.group202205.homework.spring09.service.AuthorService;
import ru.otus.group202205.homework.spring09.service.mapper.AuthorMapper;
import ru.otus.group202205.homework.spring09.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring09.web.webutil.ActionMode;
import ru.otus.group202205.homework.spring09.web.webutil.CardService;

@WebMvcTest(AuthorController.class)
@Import(AuthorTestDataComponent.class)
class AuthorControllerTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;
  @MockBean
  private CardService cardService;
  @MockBean
  private AuthorService authorService;
  @MockBean
  private AuthorMapper authorMapper;

  @BeforeEach
  void init() {
    Mockito.reset(cardService);
    Mockito.reset(authorService);
    Mockito.reset(authorMapper);
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
        .doThrow(new LibraryGeneralException("No such entity with id null",
            new NoSuchElementException()))
        .when(authorService)
        .findById(null);
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
  }

  @Test
  void shouldBeReturnAuthorCreateCardPage() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/author/get-author/card-to-create.html",
        AuthorControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders
            .get("/author")
            .queryParam("actionMode",
                "CREATE"))
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
  void shouldBeReturnCode400AuthorReadCardPageWithoutId() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/author/get-author/read-update-card-id-error.html",
        AuthorControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders
            .get("/author")
            .queryParam("actionMode",
                "READ"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isBadRequest())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist(("author")))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists(("error")))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists(("entityClass")))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(exceptedContent));
  }

  @Test
  void shouldBeReturnCode400AuthorUpdateCardPageWithoutId() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/author/get-author/read-update-card-id-error.html",
        AuthorControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders
            .get("/author")
            .queryParam("actionMode",
                "UPDATE"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isBadRequest())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("pageTitle",
                "Update author page"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("author"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("entityClass"))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(exceptedContent));
  }

  @Test
  void shouldBeReturnCode400AuthorDeleteCardPageWithoutId() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/author/get-author/delete-card-not-allowed-error.html",
        AuthorControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders
            .get("/author")
            .queryParam("actionMode",
                "DELETE"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isBadRequest())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("author"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("entityClass"))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(exceptedContent));
  }

  @Test
  void shouldBeReturnCode200AuthorReadCardPageWithId() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/author/get-author/read-author-200-id-1-read.html",
        AuthorControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    AuthorDto authorDto = authorTestDataComponent.getBulychevKirAuthorDto();
    Mockito
        .doReturn(authorDto)
        .when(authorService)
        .findById(1L);
    mvc
        .perform(MockMvcRequestBuilders
            .get("/author")
            .queryParam("id",
                "1")
            .queryParam("actionMode",
                "READ"))
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
            .attribute("author",
                authorDto))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("error"))
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
        AuthorControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    List<AuthorDto> authorDtos = authorTestDataComponent
        .getAllExistingAuthors()
        .stream()
        .map(authorMapper::toDto)
        .collect(Collectors.toList());
    Mockito
        .doReturn(authorDtos)
        .when(authorService)
        .findAll();
    mvc
        .perform(MockMvcRequestBuilders.get("/authors"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isOk())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("authors",
                authorDtos))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("entityClass"))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(exceptedContent));
  }

  @Test
  void shouldBeReturnCode302DeleteAuthorRedirectToAuthors() throws
      Exception {
    Mockito
        .doNothing()
        .when(authorService)
        .deleteById(1L);
    mvc
        .perform(MockMvcRequestBuilders
            .post("/authors/delete")
            .queryParam("id",
                "1")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isFound())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/authors?message=Author+with+id+1+deleted%21"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("authors"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("message",
                "Author with id 1 deleted!"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("entityClass"));
  }

  @Test
  void shouldBeReturnCode302CreateAuthorRedirectToAuthors() throws
      Exception {
    Author pushkinAuthor = authorTestDataComponent.getPushkinAlexSergAuthor();
    AuthorDto pushkinAuthorDto = authorMapper.toDto(pushkinAuthor);
    Mockito
        .doAnswer(invocation -> {
          AuthorDto authorDto = invocation.getArgument(0);
          authorDto.setId(3L);
          return authorDto;
        })
        .when(authorService)
        .saveOrUpdate(pushkinAuthorDto);
    mvc
        .perform(MockMvcRequestBuilders
            .post("/authors")
            .queryParam("surname",
                pushkinAuthor.getSurname())
            .queryParam("name",
                pushkinAuthor.getName())
            .queryParam("patronymic",
                pushkinAuthor.getPatronymic())
            .queryParam("birthYear",
                String.valueOf(pushkinAuthor.getBirthYear()))
            .queryParam("deathYear",
                String.valueOf(pushkinAuthor.getDeathYear()))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isFound())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/authors?message=Author+with+id+3+saved%21"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("authors"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("message",
                "Author with id 3 saved!"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("entityClass"));
  }

  @Test
  void shouldBeReturnCode302UpdateAuthorRedirectToAuthors() throws
      Exception {
    Author bulychevAuthor = authorTestDataComponent.getBulychevKirAuthor();
    bulychevAuthor.setSurname("Mojeiko");
    AuthorDto bulychevAuthorDto = authorMapper.toDto(bulychevAuthor);
    Mockito
        .doAnswer(invocation -> invocation.getArgument(0))
        .when(authorService)
        .saveOrUpdate(bulychevAuthorDto);
    mvc
        .perform(MockMvcRequestBuilders
            .post("/authors")
            .queryParam("id",
                String.valueOf(bulychevAuthor.getId()))
            .queryParam("surname",
                bulychevAuthor.getSurname())
            .queryParam("name",
                bulychevAuthor.getName())
            .queryParam("birthYear",
                String.valueOf(bulychevAuthor.getBirthYear()))
            .queryParam("deathYear",
                String.valueOf(bulychevAuthor.getDeathYear()))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isFound())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/authors?message=Author+with+id+1+saved%21"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("authors"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("message",
                "Author with id 1 saved!"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("entityClass"));
  }

  @Test
  void shouldBeReturnCode400CreateOrUpdateAuthorThrowDuplicateKeyException() throws
      Exception {
    Author bulychevAuthor = authorTestDataComponent.getBulychevKirAuthor();
    bulychevAuthor.setId(null);
    AuthorDto bulychevAuthorDto = authorMapper.toDto(bulychevAuthor);
    Mockito
        .doThrow(new LibraryGeneralException("Can't create author",
            new DuplicateKeyException("unique constraint violation")))
        .when(authorService)
        .saveOrUpdate(bulychevAuthorDto);
    mvc
        .perform(MockMvcRequestBuilders
            .post("/authors")
            .queryParam("surname",
                bulychevAuthor.getSurname())
            .queryParam("name",
                bulychevAuthor.getName())
            .queryParam("birthYear",
                String.valueOf(bulychevAuthor.getBirthYear()))
            .queryParam("deathYear",
                String.valueOf(bulychevAuthor.getDeathYear()))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isBadRequest())
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("authors",
                "message",
                "entityClass"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("error"));
  }

  @Test
  void shouldBeReturnCode400CreateOrUpdateAuthorThrowBindException() throws
      Exception {
    Author bulychevAuthor = authorTestDataComponent.getBulychevKirAuthor();
    bulychevAuthor.setId(null);
    bulychevAuthor.setPatronymic(bulychevAuthor.getPatronymic() + "()");
    mvc
        .perform(MockMvcRequestBuilders
            .post("/authors")
            .queryParam("surname",
                bulychevAuthor.getSurname())
            .queryParam("name",
                bulychevAuthor.getName())
            .queryParam("patronymic",
                bulychevAuthor.getPatronymic())
            .queryParam("birthYear",
                String.valueOf(bulychevAuthor.getBirthYear()))
            .queryParam("deathYear",
                String.valueOf(bulychevAuthor.getDeathYear()))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isBadRequest())
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("authors",
                "message"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("error",
                "entityClass"));
  }

}