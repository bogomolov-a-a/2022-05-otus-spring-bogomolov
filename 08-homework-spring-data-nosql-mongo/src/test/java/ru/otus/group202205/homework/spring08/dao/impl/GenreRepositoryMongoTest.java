package ru.otus.group202205.homework.spring08.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.mongodb.MongoWriteException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.group202205.homework.spring08.dao.DaoConfig;
import ru.otus.group202205.homework.spring08.dao.GenreRepository;
import ru.otus.group202205.homework.spring08.model.Genre;
import ru.otus.group202205.homework.spring08.testdata.GenreTestDataComponent;

@DataMongoTest
@Import({TransactionAutoConfiguration.class, DaoConfig.class,
    MongoDbRepositoryUnitTestConfig.class, GenreTestDataComponent.class})
@DirtiesContext
class GenreRepositoryMongoTest {

  private static final String INSERTED_GENRE_ID_VALUE = "3";
  private static final String EXISTING_GENRE_ID_VALUE = "2";
  private static final String SCIENCE_FICTION_UPDATED_GENRE_NAME = "SciFi";
  @Autowired
  private GenreRepository genreRepository;

  @Autowired
  private GenreTestDataComponent genreTestDataComponent;

  /*Mongock can't work(correctly) with @Transactional and mongo replicas. TransactionManager only work with replicas. */
  @BeforeEach
  void beforeEach() {
    genreRepository.deleteAll();
    genreRepository.saveAll(genreTestDataComponent.getAllExistingGenres());
  }

  //region create
  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeInsertNewGenre() {
    assertThat(genreRepository.existsById(INSERTED_GENRE_ID_VALUE)).isFalse();
    Genre exceptedGenre = genreTestDataComponent.getMangaGenre();
    assertThat(exceptedGenre.getId()).isNull();
    exceptedGenre = genreRepository.save(exceptedGenre);
    String savedGenreId = exceptedGenre.getId();
    assertThat(savedGenreId)
        .isNotNull();
    Genre actualGenre = genreRepository
        .findById(savedGenreId)
        .orElseThrow();
    assertThat(actualGenre)
        .isNotNull()
        .isEqualTo(exceptedGenre);
  }

  @Test
  void shouldBeThrowDuplicateKeyExceptionByGenreNaturalKey() {
    Genre exceptedGenre = genreTestDataComponent.getNovellGenre();
    exceptedGenre.setId(null);
    assertThatCode(() -> genreRepository.save(exceptedGenre))
        .isNotNull()
        .isInstanceOf(DuplicateKeyException.class)
        .hasCauseInstanceOf(MongoWriteException.class);
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
    actualGenre = genreRepository.save(actualGenre);
    Genre actualUpdatedGenre = genreRepository
        .findById(EXISTING_GENRE_ID_VALUE)
        .orElseThrow();
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
    assertThat(actualGenre).isEqualTo(exceptedGenre);
    genreRepository.deleteById(EXISTING_GENRE_ID_VALUE);
    assertThat(genreRepository.findById(EXISTING_GENRE_ID_VALUE))
        .isNotNull()
        .isNotPresent();
  }
  //endregion
}