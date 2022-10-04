package ru.otus.group202205.homework.spring05.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.group202205.homework.spring05.dao.GenreDao;
import ru.otus.group202205.homework.spring05.model.Genre;
import ru.otus.group202205.homework.spring05.testdata.GenreTestDataComponent;

@JdbcTest
@Import({GenreDaoJdbc.class, GenreTestDataComponent.class})
class GenreDaoJdbcTest {

  private static final Long INSERTED_GENRE_ID_VALUE = 3L;
  private static final Long EXiSTING_GENRE_ID_VALUE = 2L;
  private static final String SCIENCE_FICTION_UPDATED_GENRE_NAME = "SciFi";
  @Autowired
  private GenreDao genreDao;

  @Autowired
  private GenreTestDataComponent genreTestDataComponent;

  //region create
  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeInsertNewGenre() {
    Genre exceptedGenre = genreTestDataComponent.getMangaGenre();
    genreDao.insert(exceptedGenre);
    assertThat(exceptedGenre.getId())
        .isNotNull()
        .isEqualTo(INSERTED_GENRE_ID_VALUE);
  }

  @Test
  void shouldBeThrowDuplicateKeyExceptionByGenreNaturalKey() {
    Genre exceptedGenre = genreTestDataComponent.getNovellGenre();
    exceptedGenre.setId(null);
    assertThatCode(() -> genreDao.insert(exceptedGenre))
        .isNotNull()
        .isInstanceOf(DuplicateKeyException.class);
  }

  @Test
  void shouldBeThrowNullPointerExceptionByGenreNaturalKey() {
    Genre emptyGenre = new Genre();
    assertThatCode(() -> genreDao.insert(emptyGenre))
        .isNotNull()
        .isInstanceOf(NullPointerException.class);
  }
  //endregion

  //region read
  @Transactional(readOnly = true)
  @Test
  void shouldBeGetAllInsertedGenres() {
    assertThat(genreDao.getAll())
        .isNotNull()
        .isNotEmpty()
        .isEqualTo(genreTestDataComponent.getAllExistingGenres());
  }
  //endregion

  //region update
  @Test
  void shouldBeUpdateInsertedGenre() {
    Genre exceptedGenre = genreTestDataComponent.getScienceFictionGenre();
    assertThat(exceptedGenre.getId())
        .isNotNull()
        .isEqualTo(EXiSTING_GENRE_ID_VALUE);
    Genre actualGenre = genreDao.getById(EXiSTING_GENRE_ID_VALUE);
    assertThat(actualGenre)
        .isNotNull()
        .usingRecursiveComparison()
        .isEqualTo(exceptedGenre);
    exceptedGenre.setName(SCIENCE_FICTION_UPDATED_GENRE_NAME);
    genreDao.update(exceptedGenre);
    actualGenre = genreDao.getById(EXiSTING_GENRE_ID_VALUE);
    assertThat(actualGenre)
        .isNotNull()
        .usingRecursiveComparison()
        .isEqualTo(exceptedGenre);
  }
  //endregion

  //region delete
  @Test
  void shouldBeDeleteInsertedGenre() {
    Genre exceptedGenre = genreTestDataComponent.getScienceFictionGenre();
    Genre actualGenre = genreDao.getById(EXiSTING_GENRE_ID_VALUE);
    assertThat(actualGenre)
        .isNotNull()
        .usingRecursiveComparison()
        .isEqualTo(exceptedGenre);
    genreDao.deleteById(EXiSTING_GENRE_ID_VALUE);
    assertThatCode(() -> genreDao.getById(EXiSTING_GENRE_ID_VALUE))
        .isNotNull()
        .isInstanceOf(EmptyResultDataAccessException.class);
  }
  //endregion
}