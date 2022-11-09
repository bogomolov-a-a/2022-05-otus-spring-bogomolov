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
import org.springframework.transaction.annotation.Transactional;
import ru.otus.group202205.homework.spring08.dao.AuthorRepository;
import ru.otus.group202205.homework.spring08.dao.DaoConfig;
import ru.otus.group202205.homework.spring08.model.Author;
import ru.otus.group202205.homework.spring08.testdata.AuthorTestDataComponent;


@DataMongoTest
@Import({TransactionAutoConfiguration.class, DaoConfig.class,
    MongoDbRepositoryUnitTestConfig.class,
    AuthorTestDataComponent.class})
@DirtiesContext
class AuthorDaoMongoTest {

  private static final String EXISTING_AUTHOR_ID_VALUE = "2";
  private static final String MAKISE_KURISU_MARRIED_SURNAME = "Okabe";
  @Autowired
  private AuthorRepository authorRepository;
  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;

  /*Mongock can't work(correctly) with @Transactional and mongo replicas. TransactionManager only work with replicas. */
  @BeforeEach
  void beforeEach() {
    authorRepository.deleteAll();
    authorRepository.saveAll(authorTestDataComponent.getAllExistingAuthors());
  }

  //region create
  @Test
  void shouldBeInsertNewAuthor() {
    Author exceptedAuthor = authorTestDataComponent.getMakiseKurisuAuthor();
    assertThat(exceptedAuthor.getId()).isNull();
    exceptedAuthor = authorRepository.save(exceptedAuthor);
    String savedAuthorId = exceptedAuthor.getId();
    assertThat(savedAuthorId).isNotNull();
    Author actualAuthor = authorRepository
        .findById(savedAuthorId)
        .orElseThrow();
    assertThat(actualAuthor)
        .isNotNull()
        .isEqualTo(exceptedAuthor);
  }


  @Test
  void shouldBeInsertNewDeadAuthor() {
    Author exceptedAuthor = authorTestDataComponent.getPushkinAlexSergAuthor();
    assertThat(exceptedAuthor.getId()).isNull();
    exceptedAuthor = authorRepository.save(exceptedAuthor);
    String savedAuthorId = exceptedAuthor.getId();
    assertThat(savedAuthorId).isNotNull();
    Author actualAuthor = authorRepository
        .findById(savedAuthorId)
        .orElseThrow();
    assertThat(actualAuthor)
        .isNotNull()
        .isEqualTo(exceptedAuthor);
  }

  @Test
  void shouldBeThrowDuplicateKeyExceptionByAuthorNaturalKey() {
    Author exceptedAuthor = authorTestDataComponent.getBulychevKirAuthor();
    exceptedAuthor.setId(null);
    assertThatCode(() -> authorRepository.save(exceptedAuthor))
        .isNotNull()
        .isInstanceOf(DuplicateKeyException.class)
        .hasCauseInstanceOf(MongoWriteException.class);
  }

  //endregion

  //region read
  @Test
  @Transactional(readOnly = true)
  void shouldBeGetAllInsertedAuthors() {
    assertThat(authorRepository.findAll())
        .isNotNull()
        .isNotEmpty()
        .isEqualTo(authorTestDataComponent.getAllExistingAuthors());
  }
  //endregion

  //region update
  @Test
  void shouldBeUpdateInsertedAuthor() {
    Author exceptedAuthor = authorTestDataComponent.getMakiseKurisuAuthor();
    assertThat(exceptedAuthor.getId()).isNull();
    exceptedAuthor = authorRepository.save(exceptedAuthor);
    String savedAuthorId = exceptedAuthor.getId();
    assertThat(savedAuthorId).isNotNull();
    exceptedAuthor.setSurname(MAKISE_KURISU_MARRIED_SURNAME);
    exceptedAuthor = authorRepository.save(exceptedAuthor);
    assertThat(exceptedAuthor.getId())
        .isNotNull()
        .isEqualTo(savedAuthorId);
    Author actualAuthor = authorRepository
        .findById(savedAuthorId)
        .orElseThrow();
    assertThat(actualAuthor)
        .isNotNull()
        .isEqualTo(exceptedAuthor);
  }
  //endregion

  //region delete
  @Test
  void shouldBeDeleteInsertedAuthor() {
    Author exceptedAuthor = authorTestDataComponent.getTolstoyLevNikAuthor();
    Optional<Author> actualAuthorOptional = authorRepository.findById(EXISTING_AUTHOR_ID_VALUE);
    assertThat(actualAuthorOptional)
        .isNotNull()
        .isPresent();
    Author actualAuthor = actualAuthorOptional.get();
    assertThat(actualAuthor).isEqualTo(exceptedAuthor);
    authorRepository.deleteById(EXISTING_AUTHOR_ID_VALUE);
    assertThat(authorRepository.findById(EXISTING_AUTHOR_ID_VALUE))
        .isNotNull()
        .isNotPresent();
  }
  //endregion
}