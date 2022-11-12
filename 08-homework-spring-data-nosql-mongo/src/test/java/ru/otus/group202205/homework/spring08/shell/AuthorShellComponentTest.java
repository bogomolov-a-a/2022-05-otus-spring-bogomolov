package ru.otus.group202205.homework.spring08.shell;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

import com.mongodb.MongoException;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.shell.Shell;
import ru.otus.group202205.homework.spring08.dto.AuthorDto;
import ru.otus.group202205.homework.spring08.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring08.model.Author;
import ru.otus.group202205.homework.spring08.service.AuthorService;
import ru.otus.group202205.homework.spring08.service.converter.AuthorConverter;
import ru.otus.group202205.homework.spring08.service.mapper.AuthorMapper;
import ru.otus.group202205.homework.spring08.testdata.AuthorTestDataComponent;

@SpringBootTest(classes = {AuthorShellComponent.class, AuthorTestDataComponent.class, TestShellConfig.class})
class AuthorShellComponentTest {

  private static final String INSERTED_AUTHOR_ID_VALUE = "3";
  private static final String EXISTING_AUTHOR_ID_VALUE = "1";
  private static final String MISSING_AUTHOR_ID_VALUE = "4";
  @Autowired
  private Shell shell;
  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;
  @MockBean
  private AuthorService authorService;
  @MockBean
  private AuthorMapper authorMapper;
  @MockBean
  private AuthorConverter authorConverter;

  @BeforeEach
  void init() {
    Mockito.reset(authorMapper);
    Mockito.reset(authorConverter);
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
    Mockito
        .doAnswer(invocation -> {
          AuthorDto author = invocation.getArgument(0);
          return String.format("Author id: %s%ssurname: %s%sname: %s%spatronymic: %s%sbirth year: %d%sdeath year: %s%s",
              author.getId(),
              System.lineSeparator(),
              author.getSurname(),
              System.lineSeparator(),
              author.getName(),
              System.lineSeparator(),
              author.getPatronymic(),
              System.lineSeparator(),
              author.getBirthYear(),
              System.lineSeparator(),
              author.getDeathYear(),
              System.lineSeparator());
        })
        .when(authorConverter)
        .convertAuthor(any());
    Mockito
        .doAnswer(invocation -> {
          List<AuthorDto> authors = invocation.getArgument(0);
          StringBuilder result = new StringBuilder("Author list").append(System.lineSeparator());
          authors.forEach(author -> result.append(authorConverter.convertAuthor(author)));
          return result.toString();
        })
        .when(authorConverter)
        .convertAuthors(anyList());
  }

  //region create
  @Test
  void shouldBeInsertNewAuthor() {
    Author exceptedAuthor = authorTestDataComponent.getMakiseKurisuAuthor();
    AuthorDto exceptedAuthorDto = authorMapper.toDto(exceptedAuthor);
    Mockito
        .doAnswer(invocation -> {
          AuthorDto result = invocation.getArgument(0);
          result.setId(INSERTED_AUTHOR_ID_VALUE);
          return result;
        })
        .when(authorService)
        .saveOrUpdate(exceptedAuthorDto);
    String successCreatedAuthorMessage = (String) shell.evaluate(() -> String.format("create-author --surname %s --name %s --birth-year %d",
        exceptedAuthor.getSurname(),
        exceptedAuthor.getName(),
        exceptedAuthor.getBirthYear()));
    assertThat(successCreatedAuthorMessage)
        .isNotNull()
        .isEqualTo("Author with id " + INSERTED_AUTHOR_ID_VALUE + " created!");
  }

  @Test
  void shouldBeThrowDuplicateKeyExceptionByAuthorNaturalKey() {
    Author exceptedAuthor = authorTestDataComponent.getBulychevKirAuthor();
    assertThat(exceptedAuthor.getId())
        .isNotNull()
        .isEqualTo(EXISTING_AUTHOR_ID_VALUE);
    exceptedAuthor.setId(null);
    AuthorDto exceptedAuthorDto = authorMapper.toDto(exceptedAuthor);
    Mockito
        .doThrow(new LibraryGeneralException("Can't insert or update author",
            new DuplicateKeyException("unique key violation!")))
        .when(authorService)
        .saveOrUpdate(exceptedAuthorDto);
    String naturalKeyViolationMessage = (String) shell.evaluate(() -> String.format("create-author --surname %s --name %s --birth-year %d --death-year %d",
        exceptedAuthor.getSurname(),
        exceptedAuthor.getName(),
        exceptedAuthor.getBirthYear(),
        exceptedAuthor.getDeathYear()));
    assertThat(naturalKeyViolationMessage)
        .isNotNull()
        .isEqualTo("Can't insert or update author Cause unique key violation!");
  }
  //endregion

  //region read
  @Test
  void shouldBeReadAllInsertedAuthors() {
    List<AuthorDto> authorDtos = authorTestDataComponent
        .getAllExistingAuthors()
        .stream()
        .map(authorMapper::toDto)
        .collect(Collectors.toList());
    Mockito
        .doReturn(authorDtos)
        .when(authorService)
        .findAll();
    String actualReadAuthorsOutput = (String) shell.evaluate(() -> "read-authors");
    assertThat(actualReadAuthorsOutput)
        .isNotNull()
        .isEqualTo("Author list" + System.lineSeparator() + "Author id: 1" + System.lineSeparator() + "surname: Bulychev" + System.lineSeparator() + "name: Kir"
            + System.lineSeparator() + "patronymic: not set" + System.lineSeparator() + "birth year: 1934" + System.lineSeparator() + "death year: 2003"
            + System.lineSeparator() + "Author id: 2" + System.lineSeparator() + "surname: Tolstoy" + System.lineSeparator() + "name: Lev"
            + System.lineSeparator() + "patronymic: Nikolayevich" + System.lineSeparator() + "birth year: 1828" + System.lineSeparator() + "death year: 1910"
            + System.lineSeparator());
  }

  @Test
  void shouldBeReadAuthorById() {
    Author exceptedAuthor = authorTestDataComponent.getBulychevKirAuthor();
    Mockito
        .doReturn(authorMapper.toDto(exceptedAuthor))
        .when(authorService)
        .findById(EXISTING_AUTHOR_ID_VALUE);
    String actualReadAuthorsOutput = (String) shell.evaluate(() -> "read-author --id " + EXISTING_AUTHOR_ID_VALUE);
    assertThat(actualReadAuthorsOutput)
        .isNotNull()
        .isEqualTo("Author id: 1" + System.lineSeparator() + "surname: Bulychev" + System.lineSeparator() + "name: Kir" + System.lineSeparator()
            + "patronymic: not set" + System.lineSeparator() + "birth year: 1934" + System.lineSeparator() + "death year: 2003" + System.lineSeparator());
  }

  @Test
  void shouldBeThrowMongoExceptionByGetAllAuthors() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't read all authors",
            new MongoException("table or view not exists")))
        .when(authorService)
        .findAll();
    String actualReadAuthorErrorMessage = (String) shell.evaluate(() -> "read-authors");
    assertThat(actualReadAuthorErrorMessage)
        .isNotNull()
        .isEqualTo("Can't read all authors Cause table or view not exists");
  }
  //endregion

  //region update
  @Test
  void shouldBeUpdateAuthor() {
    Author exceptedAuthor = new Author();
    exceptedAuthor.setId(EXISTING_AUTHOR_ID_VALUE);
    exceptedAuthor.setPatronymic("Vsevolodovich");
    exceptedAuthor.setName("Igor");
    exceptedAuthor.setSurname("Mojeiko");
    AuthorDto exceptedAuthorDto = authorMapper.toDto(exceptedAuthor);
    Mockito
        .doAnswer(invocation -> {
          AuthorDto result = invocation.getArgument(0);
          result.setId(EXISTING_AUTHOR_ID_VALUE);
          return result;
        })
        .when(authorService)
        .saveOrUpdate(exceptedAuthorDto);
    String actualUpdateErrorMessage =
        (String) shell.evaluate(() -> "update-author --id " + EXISTING_AUTHOR_ID_VALUE + " --surname Mojeiko --name Igor --patronymic Vsevolodovich");
    assertThat(actualUpdateErrorMessage)
        .isNotNull()
        .isEqualTo("Author with id " + EXISTING_AUTHOR_ID_VALUE + " updated!");
  }

  @Test
  void shouldBeThrowMongoExceptionInUpdateAuthor() {
    Author emptyAuthor = new Author();
    emptyAuthor.setId(MISSING_AUTHOR_ID_VALUE);
    emptyAuthor.setPatronymic(null);
    AuthorDto exceptedAuthorDto = authorMapper.toDto(emptyAuthor);
    Mockito
        .doThrow(new LibraryGeneralException("Can't update author",
            new MongoException("Not found author with id " + MISSING_AUTHOR_ID_VALUE)))
        .when(authorService)
        .saveOrUpdate(exceptedAuthorDto);
    String actualUpdateErrorMessage = (String) shell.evaluate(() -> "update-author --id " + MISSING_AUTHOR_ID_VALUE);
    assertThat(actualUpdateErrorMessage)
        .isNotNull()
        .isEqualTo("Can't update author Cause Not found author with id " + MISSING_AUTHOR_ID_VALUE);
  }
  //endregion

  //region delete
  @Test
  void shouldBeDeleteExistsAuthor() {
    Mockito
        .doNothing()
        .when(authorService)
        .deleteById(EXISTING_AUTHOR_ID_VALUE);
    String successDeletedAuthorMessage = (String) shell.evaluate(() -> "delete-author --id " + EXISTING_AUTHOR_ID_VALUE);
    assertThat(successDeletedAuthorMessage)
        .isNotNull()
        .isEqualTo("Author with id " + EXISTING_AUTHOR_ID_VALUE + " deleted!");
  }

  @Test
  void shouldBeThrownPersistenceExceptionInDeleteAuthorById() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't delete author",
            new MongoException("Not found author with id " + MISSING_AUTHOR_ID_VALUE)))
        .when(authorService)
        .deleteById(MISSING_AUTHOR_ID_VALUE);
    String successDeletedAuthorMessage = (String) shell.evaluate(() -> "delete-author --id " + MISSING_AUTHOR_ID_VALUE);
    assertThat(successDeletedAuthorMessage)
        .isNotNull()
        .isEqualTo("Can't delete author Cause Not found author with id " + MISSING_AUTHOR_ID_VALUE);
  }
  //endregion

}