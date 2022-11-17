package ru.otus.group202205.homework.spring10.web.rest;

import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.group202205.homework.spring10.dto.AuthorDto;
import ru.otus.group202205.homework.spring10.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring10.model.Author;
import ru.otus.group202205.homework.spring10.service.AuthorService;
import ru.otus.group202205.homework.spring10.service.mapper.AuthorMapper;
import ru.otus.group202205.homework.spring10.testdata.AuthorTestDataComponent;

@WebMvcTest(AuthorController.class)
@Import(AuthorTestDataComponent.class)
class AuthorControllerTest {

  private static final ObjectWriter AUTHOR_DTO_OBJECT_WRITER;
  private static final ObjectWriter AUTHOR_DTO_LIST_OBJECT_WRITER;

  static {
    ObjectMapper mapper = new ObjectMapper();
    AUTHOR_DTO_OBJECT_WRITER = mapper
        .writer()
        .forType(AuthorDto.class);
    AUTHOR_DTO_LIST_OBJECT_WRITER = mapper
        .writer()
        .forType(TypeFactory
            .defaultInstance()
            .constructCollectionType(List.class,
                AuthorDto.class));
  }

  @Autowired
  private MockMvc mvc;
  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;
  @MockBean
  private AuthorService authorService;
  @MockBean
  private AuthorMapper authorMapper;

  @BeforeEach
  void init() {

    Mockito.reset(authorService);
    Mockito.reset(authorMapper);

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
  void shouldBeReturnCode200AuthorReadCardPageWithId() throws
      Exception {
    AuthorDto authorDto = authorTestDataComponent.getBulychevKirAuthorDto();
    Mockito
        .doReturn(authorDto)
        .when(authorService)
        .findById(1L);
    mvc
        .perform(MockMvcRequestBuilders
            .get("/authors/1"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isOk())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .content()
            .json(AUTHOR_DTO_OBJECT_WRITER.writeValueAsString(authorDto)));
  }

  @Test
  void shouldBeReturnCode200ReadAuthors() throws
      Exception {
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
            .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .content()
            .json(AUTHOR_DTO_LIST_OBJECT_WRITER.writeValueAsString(authorDtos)));
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
            .delete("/authors/1"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isFound())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/authors/page"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("authors",
                "error",
                "message",
                "entityClass"));
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
        .andExpect(MockMvcResultMatchers.redirectedUrl("/authors/page"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("authors",
                "error",
                "entityClass",
                "message"));
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
        .andExpect(MockMvcResultMatchers.redirectedUrl("/authors/page"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("authors",
                "error",
                "entityClass",
                "message"));
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