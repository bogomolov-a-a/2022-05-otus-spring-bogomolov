package ru.otus.group202205.homework.spring05.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.group202205.homework.spring05.dao.impl.GenreDaoJdbc;
import ru.otus.group202205.homework.spring05.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring05.model.Genre;
import ru.otus.group202205.homework.spring05.service.GenreService;
import ru.otus.group202205.homework.spring05.testdata.GenreTestDataComponent;

@SpringBootTest(classes = {GenreServiceImpl.class, GenreTestDataComponent.class})
class GenreServiceImplTest {

  private static final Long INSERTED_GENRE_ID_VALUE = 3L;
  private static final Long EXISTING_GENRE_ID_VALUE = 2L;
  private static final Long MISSING_GENRE_ID_VALUE = 4L;
  private static final String LITE_NOVELL_UPDATED_GENRE_NAME = "Ranobe";

  @Autowired
  private GenreService genreService;
  @Autowired
  private GenreTestDataComponent genreTestDataComponent;
  @MockBean
  private GenreDaoJdbc genreDaoJdbc;

  //region create
  @Test
  void shouldBeInsertNewGenre() {
    Genre actualGenre = genreTestDataComponent.getMangaGenre();
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          ((Genre) args[0]).setId(INSERTED_GENRE_ID_VALUE);
          return null;
        })
        .when(genreDaoJdbc)
        .insert(actualGenre);
    genreService.insert(actualGenre);
    Genre exceptedGenre = genreTestDataComponent.getMangaGenre();
    exceptedGenre.setId(INSERTED_GENRE_ID_VALUE);
    assertThat(actualGenre)
        .isNotNull()
        .isEqualTo(exceptedGenre);
  }

  @Test
  void shouldBeThrowDuplicateKeyExceptionByGenreNaturalKey() {
    Genre exceptedGenre = genreTestDataComponent.getNovellGenre();
    Mockito
        .doThrow(DuplicateKeyException.class)
        .when(genreDaoJdbc)
        .insert(exceptedGenre);
    assertThatCode(() -> genreService.insert(exceptedGenre))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(DuplicateKeyException.class);
  }
  //endregion

  //region read
  @Test
  void shouldBeGetAllInsertedGenres() {
    Mockito
        .doReturn(genreTestDataComponent.getAllExistingGenres())
        .when(genreDaoJdbc)
        .getAll();
    assertThat(genreService.getAll())
        .isNotNull()
        .isNotEmpty()
        .isEqualTo(genreTestDataComponent.getAllExistingGenres());
  }

  @Test
  void shouldBeThrowDataRetrievalFailureExceptionInGetAllGenres() {
    Mockito
        .doThrow(DataRetrievalFailureException.class)
        .when(genreDaoJdbc)
        .getAll();
    assertThatCode(() -> genreService.getAll())
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(DataRetrievalFailureException.class);
  }
  //endregion

  //region update
  @Test
  void shouldBeUpdateInsertedGenreName() {
    Genre insertedGenre = genreTestDataComponent.getLiteNovellGenre();
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          ((Genre) args[0]).setId(INSERTED_GENRE_ID_VALUE);
          return null;
        })
        .when(genreDaoJdbc)
        .insert(insertedGenre);
    genreService.insert(insertedGenre);
    assertThat(insertedGenre.getId())
        .isNotNull()
        .isEqualTo(INSERTED_GENRE_ID_VALUE);
    Genre exceptedUpdatedAuthor = new Genre();
    exceptedUpdatedAuthor.setId(INSERTED_GENRE_ID_VALUE);
    exceptedUpdatedAuthor.setName(LITE_NOVELL_UPDATED_GENRE_NAME);
    Mockito
        .doNothing()
        .when(genreDaoJdbc)
        .update(exceptedUpdatedAuthor);
    Mockito
        .doReturn(insertedGenre)
        .when(genreDaoJdbc)
        .getById(INSERTED_GENRE_ID_VALUE);
    genreService.update(exceptedUpdatedAuthor);
    assertThat(exceptedUpdatedAuthor.getId())
        .isNotNull()
        .isEqualTo(INSERTED_GENRE_ID_VALUE);
    Mockito.reset(genreDaoJdbc);
    Mockito
        .doReturn(exceptedUpdatedAuthor)
        .when(genreDaoJdbc)
        .getById(INSERTED_GENRE_ID_VALUE);
    Genre actualUpdatedGenre = genreService.getById(INSERTED_GENRE_ID_VALUE);
    assertThat(actualUpdatedGenre)
        .isNotNull()
        .isEqualTo(exceptedUpdatedAuthor);
  }

  @Test
  void shouldBeDataRetrievalFailureExceptionInUpdateGenre() {
    Genre emptyGenre = new Genre();
    Mockito
        .doThrow(DataRetrievalFailureException.class)
        .when(genreDaoJdbc)
        .update(emptyGenre);
    assertThatCode(() -> genreService.update(emptyGenre))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(DataRetrievalFailureException.class);
  }
  //endregion

  //region delete
  @Test
  void shouldBeDeleteInsertedGenre() {
    Genre exceptedGenre = genreTestDataComponent.getScienceFictionGenre();
    assertThat(exceptedGenre.getId())
        .isNotNull()
        .isEqualTo(EXISTING_GENRE_ID_VALUE);
    Mockito
        .doReturn(exceptedGenre)
        .when(genreDaoJdbc)
        .getById(EXISTING_GENRE_ID_VALUE);
    Genre actualGenre = genreService.getById(EXISTING_GENRE_ID_VALUE);
    assertThat(actualGenre)
        .usingRecursiveComparison()
        .isEqualTo(exceptedGenre);
    Mockito
        .doNothing()
        .when(genreDaoJdbc)
        .deleteById(EXISTING_GENRE_ID_VALUE);
    genreService.deleteById(EXISTING_GENRE_ID_VALUE);
    Mockito.reset(genreDaoJdbc);
    Mockito
        .doThrow(EmptyResultDataAccessException.class)
        .when(genreDaoJdbc)
        .getById(EXISTING_GENRE_ID_VALUE);
    assertThatCode(() -> genreService.getById(EXISTING_GENRE_ID_VALUE))
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(EmptyResultDataAccessException.class);
  }


  @Test
  void shouldBeDataRetrievalFailureExceptionInDeleteGenreById() {
    Mockito
        .doThrow(DataRetrievalFailureException.class)
        .when(genreDaoJdbc)
        .deleteById(MISSING_GENRE_ID_VALUE);
    assertThatCode(() -> genreService.deleteById(MISSING_GENRE_ID_VALUE))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(DataRetrievalFailureException.class);
  }
  //endregion
}