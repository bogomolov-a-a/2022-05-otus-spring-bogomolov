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
import ru.otus.group202205.homework.spring05.model.Genre;
import ru.otus.group202205.homework.spring05.service.GenreService;
import ru.otus.group202205.homework.spring05.service.impl.GenreConverterImpl;
import ru.otus.group202205.homework.spring05.testdata.GenreTestDataComponent;

@SpringBootTest(classes = {GenreShellComponent.class, GenreConverterImpl.class, GenreTestDataComponent.class, TestShellConfig.class})
class GenreShellComponentTest {

  private static final Long INSERTED_GENRE_ID_VALUE = 3L;
  private static final Long EXISTING_GENRE_ID_VALUE = 1L;
  private static final Long MISSING_GENRE_ID_VALUE = 4L;
  private static final String NOVELL_UPDATED_GENRE_NAME = "Ranobe";
  @Autowired
  private Shell shell;
  @Autowired
  private GenreTestDataComponent genreTestDataComponent;
  @MockBean
  private GenreService genreService;

  //region create
  @Test
  void shouldBeInsertNewGenre() {
    Genre exceptedGenre = genreTestDataComponent.getMangaGenre();
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          ((Genre) args[0]).setId(INSERTED_GENRE_ID_VALUE);
          return null;
        })
        .when(genreService)
        .insert(exceptedGenre);
    String successCreatedGenreMessage = (String) shell.evaluate(() -> String.format("create-genre --name %s",
        exceptedGenre.getName()));
    assertThat(successCreatedGenreMessage)
        .isNotNull()
        .isEqualTo("Genre with id " + INSERTED_GENRE_ID_VALUE + " created!");
  }


  @Test
  void shouldBeThrowDuplicateKeyExceptionByGenreNaturalKey() {
    Genre exceptedGenre = genreTestDataComponent.getNovellGenre();
    assertThat(exceptedGenre.getId())
        .isNotNull()
        .isEqualTo(EXISTING_GENRE_ID_VALUE);
    exceptedGenre.setId(null);
    Mockito
        .doThrow(new LibraryGeneralException("Can't insert genre",
            new DuplicateKeyException("unique key violation!")))
        .when(genreService)
        .insert(exceptedGenre);
    String naturalKeyViolationMessage = (String) shell.evaluate(() -> String.format("create-genre --name %s",
        exceptedGenre.getName()));
    assertThat(naturalKeyViolationMessage)
        .isNotNull()
        .isEqualTo("Can't insert genre Cause unique key violation!");
  }
  //endregion

  //region read
  @Test
  void shouldBeReadAllInsertedGenres() {
    Mockito
        .doReturn(genreTestDataComponent.getAllExistingGenres())
        .when(genreService)
        .getAll();
    String actualReadGenresOutput = (String) shell.evaluate(() -> "read-genres");
    assertThat(actualReadGenresOutput)
        .isNotNull()
        .isEqualTo("Genre list" + System.lineSeparator() + "Genre id: 1" + System.lineSeparator() + "name: Novell" + System.lineSeparator() + "Genre id: 2"
            + System.lineSeparator() + "name: Science fiction" + System.lineSeparator());
  }

  @Test
  void shouldBeReadGenreById() {
    Genre exceptedGenre = genreTestDataComponent.getNovellGenre();
    Mockito
        .doReturn(exceptedGenre)
        .when(genreService)
        .getById(EXISTING_GENRE_ID_VALUE);
    String actualReadAuthorsOutput = (String) shell.evaluate(() -> String.format("read-genre --id %d",
        EXISTING_GENRE_ID_VALUE));
    assertThat(actualReadAuthorsOutput)
        .isNotNull()
        .isEqualTo("Genre id: 1" + System.lineSeparator() + "name: Novell" + System.lineSeparator());
  }

  @Test
  void shouldBeThrowDataAccessExceptionByGetGenreByIdFour() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't read all genre by id " + MISSING_GENRE_ID_VALUE,
            new DataRetrievalFailureException("table or view not exists")))
        .when(genreService)
        .getById(MISSING_GENRE_ID_VALUE);
    String actualReadGenreErrorMessage = (String) shell.evaluate(() -> String.format("read-genre --id %d",
        MISSING_GENRE_ID_VALUE));
    assertThat(actualReadGenreErrorMessage)
        .isNotNull()
        .isEqualTo("Can't read all genre by id " + MISSING_GENRE_ID_VALUE + " Cause table or view not exists");
  }

  @Test
  void shouldBeThrowDataAccessExceptionByGetAllGenres() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't read all genres",
            new DataRetrievalFailureException("table or view not exists")))
        .when(genreService)
        .getAll();
    String actualReadGenreErrorMessage = (String) shell.evaluate(() -> "read-genres");
    assertThat(actualReadGenreErrorMessage)
        .isNotNull()
        .isEqualTo("Can't read all genres Cause table or view not exists");
  }
  //endregion

  //region update
  @Test
  void shouldBeUpdateGenre() {
    Genre exceptedGenre = genreTestDataComponent.getNovellGenre();
    assertThat(exceptedGenre.getId())
        .isNotNull()
        .isEqualTo(EXISTING_GENRE_ID_VALUE);
    String actualUpdateErrorMessage = (String) shell.evaluate(() -> String.format("update-genre --id %d --name %s",
        exceptedGenre.getId(),
        NOVELL_UPDATED_GENRE_NAME));
    assertThat(actualUpdateErrorMessage)
        .isNotNull()
        .isEqualTo("Genre with id " + EXISTING_GENRE_ID_VALUE + " updated!");
  }

  @Test
  void shouldBeThrowDataRetrievalFailureExceptionInUpdateGenre() {
    Genre exceptedGenre = new Genre();
    exceptedGenre.setId(MISSING_GENRE_ID_VALUE);
    exceptedGenre.setName(NOVELL_UPDATED_GENRE_NAME);
    Mockito
        .doThrow(new LibraryGeneralException("Can't update genre",
            new DataRetrievalFailureException("Not found author with id " + MISSING_GENRE_ID_VALUE)))
        .when(genreService)
        .update(exceptedGenre);
    assertThat(exceptedGenre.getId())
        .isNotNull()
        .isEqualTo(MISSING_GENRE_ID_VALUE);
    String actualUpdateErrorMessage = (String) shell.evaluate(() -> String.format("update-genre --id %d --name %s",
        exceptedGenre.getId(),
        NOVELL_UPDATED_GENRE_NAME));
    assertThat(actualUpdateErrorMessage)
        .isNotNull()
        .isEqualTo("Can't update genre Cause Not found author with id " + MISSING_GENRE_ID_VALUE);
  }
  //endregion

  //region delete
  @Test
  void shouldBeDeleteExistsGenre() {
    Mockito
        .doNothing()
        .when(genreService)
        .deleteById(EXISTING_GENRE_ID_VALUE);
    String successDeletedGenreMessage = (String) shell.evaluate(() -> String.format("delete-genre --id %d",
        EXISTING_GENRE_ID_VALUE));
    assertThat(successDeletedGenreMessage)
        .isNotNull()
        .isEqualTo("Genre with id " + EXISTING_GENRE_ID_VALUE + " deleted!");
  }

  @Test
  void shouldBeThrownDataRetrievalFailureExceptionInDeleteGenreByIde() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't delete author",
            new DataRetrievalFailureException("Not found author with id " + MISSING_GENRE_ID_VALUE)))
        .when(genreService)
        .deleteById(MISSING_GENRE_ID_VALUE);
    String successDeletedGenreMessage = (String) shell.evaluate(() -> String.format("delete-genre --id %d",
        MISSING_GENRE_ID_VALUE));
    assertThat(successDeletedGenreMessage)
        .isNotNull()
        .isEqualTo("Can't delete author Cause Not found author with id " + MISSING_GENRE_ID_VALUE);
  }
  //endregion

}