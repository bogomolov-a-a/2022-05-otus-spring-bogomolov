package ru.otus.group202205.homework.spring06.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Optional;
import javax.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.group202205.homework.spring06.dao.GenreRepository;
import ru.otus.group202205.homework.spring06.model.Genre;
import ru.otus.group202205.homework.spring06.testdata.GenreTestDataComponent;

@DataJpaTest
@Import({GenreRepositoryJpa.class, GenreTestDataComponent.class})
class GenreRepositoryJpaTest {

  private static final Long INSERTED_GENRE_ID_VALUE = 3L;
  private static final Long EXISTING_GENRE_ID_VALUE = 2L;
  private static final String SCIENCE_FICTION_UPDATED_GENRE_NAME = "SciFi";
  @Autowired
  private GenreRepository genreRepository;

  @Autowired
  private TestEntityManager testEntityManager;
  @Autowired
  private GenreTestDataComponent genreTestDataComponent;

  //region create
  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeInsertNewGenre() {
    assertThat(testEntityManager.find(Genre.class,
        INSERTED_GENRE_ID_VALUE)).isNull();
    Genre exceptedGenre = genreTestDataComponent.getMangaGenre();
    assertThat(exceptedGenre.getId()).isNull();
    exceptedGenre = genreRepository.saveOrUpdate(exceptedGenre);
    assertThat(exceptedGenre.getId())
        .isNotNull()
        .isEqualTo(INSERTED_GENRE_ID_VALUE);
    Genre actualGenre = testEntityManager.find(Genre.class,
        INSERTED_GENRE_ID_VALUE);
    assertThat(actualGenre)
        .isNotNull()
        .isEqualTo(exceptedGenre);
  }

  //fool guard for saveOrUpdate method
  @Test
  void shouldBeNotThrowPersistentObjectExceptionForDetachedEntity() {
    Genre existingGenre = testEntityManager.find(Genre.class,
        EXISTING_GENRE_ID_VALUE);
    testEntityManager.detach(existingGenre);
    assertThatCode(() -> genreRepository.saveOrUpdate(existingGenre))
        .isNull();
  }

  @Test
  void shouldBeThrowDuplicateConstraintViolationByGenreNaturalKey() {
    Genre exceptedGenre = genreTestDataComponent.getNovellGenre();
    exceptedGenre.setId(null);
    assertThatCode(() -> genreRepository.saveOrUpdate(exceptedGenre))
        .isNotNull()
        .isInstanceOf(PersistenceException.class)
        .hasCauseInstanceOf(ConstraintViolationException.class);
  }

  @Test
  void shouldBeThrowDataIntegrityConstraintViolationByGenreNaturalKey() {
    Genre emptyGenre = new Genre();
    assertThatCode(() -> genreRepository.saveOrUpdate(emptyGenre))
        .isNotNull()
        .isInstanceOf(PersistenceException.class)
        .hasCauseInstanceOf(ConstraintViolationException.class);
  }
  //endregion

  //region read
  @Transactional(readOnly = true)
  @Test
  void shouldBeGetAllInsertedGenres() {
    assertThat(genreRepository.findAll())
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
        .isEqualTo(EXISTING_GENRE_ID_VALUE);
    Optional<Genre> actualGenreOptional = genreRepository.findById(EXISTING_GENRE_ID_VALUE);
    assertThat(actualGenreOptional)
        .isNotNull()
        .isPresent();
    Genre actualGenre = actualGenreOptional.get();
    assertThat(actualGenre)
        .isNotNull()
        .isEqualTo(exceptedGenre);
    actualGenre.setName(SCIENCE_FICTION_UPDATED_GENRE_NAME);
    actualGenre = genreRepository.saveOrUpdate(actualGenre);
    Genre actualUpdatedGenre = testEntityManager.find(Genre.class,
        EXISTING_GENRE_ID_VALUE);
    assertThat(actualUpdatedGenre)
        .isNotNull()
        .isEqualTo(actualGenre);
  }
  //endregion

  //region delete
  @Test
  void shouldBeDeleteInsertedGenre() {
    Genre exceptedGenre = genreTestDataComponent.getScienceFictionGenre();
    Optional<Genre> actualGenreOptional = genreRepository.findById(EXISTING_GENRE_ID_VALUE);
    assertThat(actualGenreOptional)
        .isNotNull()
        .isPresent();
    Genre actualGenre = actualGenreOptional.get();
    assertThat(actualGenre)
        .isEqualTo(exceptedGenre);
    genreRepository.deleteById(EXISTING_GENRE_ID_VALUE);
    assertThat(genreRepository.findById(EXISTING_GENRE_ID_VALUE))
        .isNotNull()
        .isNotPresent();
  }
  //endregion
}