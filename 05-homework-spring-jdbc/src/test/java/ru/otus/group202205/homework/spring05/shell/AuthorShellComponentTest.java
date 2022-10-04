package ru.otus.group202205.homework.spring05.shell;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.shell.Shell;
import ru.otus.group202205.homework.spring05.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring05.model.Author;
import ru.otus.group202205.homework.spring05.service.AuthorService;
import ru.otus.group202205.homework.spring05.service.impl.AuthorConverterImpl;
import ru.otus.group202205.homework.spring05.testdata.AuthorTestDataComponent;

@SpringBootTest(classes = {AuthorShellComponent.class, AuthorConverterImpl.class, AuthorTestDataComponent.class, TestShellConfig.class})
class AuthorShellComponentTest {

  private static final Long INSERTED_AUTHOR_ID_VALUE = 3L;
  private static final Long EXISTING_AUTHOR_ID_VALUE = 1L;
  private static final Long MISSING_AUTHOR_ID_VALUE = 4L;
  @Autowired
  private Shell shell;
  @MockBean
  private AuthorService authorService;
  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;

  //region create
  @Test
  void shouldBeInsertNewAuthor() {
    Author exceptedAuthor = authorTestDataComponent.getMakiseKurisuAuthor();
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          ((Author) args[0]).setId(INSERTED_AUTHOR_ID_VALUE);
          return null;
        })
        .when(authorService)
        .insert(exceptedAuthor);
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
    Mockito
        .doThrow(new LibraryGeneralException("Can't insert author",
            new DuplicateKeyException("unique key violation!")))
        .when(authorService)
        .insert(exceptedAuthor);
    String naturalKeyViolationMessage = (String) shell.evaluate(() -> String.format("create-author --surname %s --name %s --birth-year %d --death-year %d",
        exceptedAuthor.getSurname(),
        exceptedAuthor.getName(),
        exceptedAuthor.getBirthYear(),
        exceptedAuthor.getDeathYear()));
    assertThat(naturalKeyViolationMessage)
        .isNotNull()
        .isEqualTo("Can't insert author Cause unique key violation!");
  }
  //endregion

  //region read
  @Test
  void shouldBeReadAllInsertedAuthors() {
    Mockito
        .doReturn(authorTestDataComponent.getAllExistingAuthors())
        .when(authorService)
        .getAll();
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
        .doReturn(exceptedAuthor)
        .when(authorService)
        .getById(EXISTING_AUTHOR_ID_VALUE);
    String actualReadAuthorsOutput = (String) shell.evaluate(() -> "read-author --id " + EXISTING_AUTHOR_ID_VALUE);
    assertThat(actualReadAuthorsOutput)
        .isNotNull()
        .isEqualTo("Author id: 1" + System.lineSeparator() + "surname: Bulychev" + System.lineSeparator() + "name: Kir" + System.lineSeparator()
            + "patronymic: not set" + System.lineSeparator() + "birth year: 1934" + System.lineSeparator() + "death year: 2003" + System.lineSeparator());
  }

  @Test
  void shouldBeThrowDataAccessExceptionByGetAuthorByIdFour() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't read all author by id 4",
            new DataRetrievalFailureException("table or view not exists")))
        .when(authorService)
        .getById(MISSING_AUTHOR_ID_VALUE);
    String actualReadAuthorErrorMessage = (String) shell.evaluate(() -> "read-author --id " + MISSING_AUTHOR_ID_VALUE);
    assertThat(actualReadAuthorErrorMessage)
        .isNotNull()
        .isEqualTo("Can't read all author by id 4 Cause table or view not exists");
  }

  @Test
  void shouldBeThrowDataAccessExceptionByGetAllAuthors() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't read all authors",
            new DataRetrievalFailureException("table or view not exists")))
        .when(authorService)
        .getAll();
    String actualReadAuthorErrorMessage = (String) shell.evaluate(() -> "read-authors");
    assertThat(actualReadAuthorErrorMessage)
        .isNotNull()
        .isEqualTo("Can't read all authors Cause table or view not exists");
  }
  //endregion

  //region update
  @Test
  void shouldBeUpdateAuthor() {
    Author author = new Author();
    author.setId(EXISTING_AUTHOR_ID_VALUE);
    author.setPatronymic("Vsevolodovich");
    author.setName("Igor");
    author.setSurname("Mojeiko");
    String actualUpdateErrorMessage = (String) shell.evaluate(() -> "update-author --id " + EXISTING_AUTHOR_ID_VALUE);
    assertThat(actualUpdateErrorMessage)
        .isNotNull()
        .isEqualTo("Author with id " + EXISTING_AUTHOR_ID_VALUE + " updated!");
  }

  @Test
  void shouldBeThrowDataRetrievalFailureExceptionInUpdateAuthor() {
    Author emptyAuthor = new Author();
    emptyAuthor.setId(MISSING_AUTHOR_ID_VALUE);
    emptyAuthor.setPatronymic(null);
    Mockito
        .doThrow(new LibraryGeneralException("Can't update author",
            new DataRetrievalFailureException("Not found author with id " + MISSING_AUTHOR_ID_VALUE)))
        .when(authorService)
        .update(emptyAuthor);
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
  void shouldBeThrownDataRetrievalFailureExceptionInDeleteAuthorById() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't delete author",
            new DataRetrievalFailureException("Not found author with id " + MISSING_AUTHOR_ID_VALUE)))
        .when(authorService)
        .deleteById(MISSING_AUTHOR_ID_VALUE);
    String successDeletedAuthorMessage = (String) shell.evaluate(() -> "delete-author --id " + MISSING_AUTHOR_ID_VALUE);
    assertThat(successDeletedAuthorMessage)
        .isNotNull()
        .isEqualTo("Can't delete author Cause Not found author with id " + MISSING_AUTHOR_ID_VALUE);
  }
  //endregion

}