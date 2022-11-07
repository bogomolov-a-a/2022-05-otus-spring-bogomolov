package ru.otus.group202205.homework.spring07.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Optional;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import ru.otus.group202205.homework.spring07.dao.AuthorRepository;
import ru.otus.group202205.homework.spring07.dao.DaoConfig;
import ru.otus.group202205.homework.spring07.model.Author;
import ru.otus.group202205.homework.spring07.testdata.AuthorTestDataComponent;

@DataJpaTest
@Import({JpaRepositoriesAutoConfiguration.class, AuthorTestDataComponent.class, DaoConfig.class})
class AuthorDaoJpaTest {

  private static final Long INSERTED_AUTHOR_ID_VALUE = 3L;
  private static final Long EXISTING_AUTHOR_ID_VALUE = 2L;
  private static final String MAKISE_KURISU_MARRIED_SURNAME = "Okabe";
  @Autowired
  private AuthorRepository authorRepository;
  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;
  @Autowired
  private TestEntityManager testEntityManager;

  //region create
  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeInsertNewAuthor() {
    assertThat(testEntityManager.find(Author.class,
        INSERTED_AUTHOR_ID_VALUE)).isNull();
    Author exceptedAuthor = authorTestDataComponent.getMakiseKurisuAuthor();
    assertThat(exceptedAuthor.getId()).isNull();
    exceptedAuthor = authorRepository.save(exceptedAuthor);
    assertThat(exceptedAuthor.getId())
        .isNotNull()
        .isEqualTo(INSERTED_AUTHOR_ID_VALUE);
    Author actualAuthor = testEntityManager.find(Author.class,
        INSERTED_AUTHOR_ID_VALUE);
    assertThat(actualAuthor)
        .isNotNull()
        .isEqualTo(exceptedAuthor);
  }

  //fool guard for save method
  @Test
  void shouldBeNotThrowPersistentObjectExceptionForDetachedEntity() {
    Author existingAuthor = testEntityManager.find(Author.class,
        EXISTING_AUTHOR_ID_VALUE);
    testEntityManager.detach(existingAuthor);
    assertThatCode(() -> authorRepository.save(existingAuthor))
        .isNull();
  }

  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeInsertNewDeadAuthor() {
    assertThat(testEntityManager.find(Author.class,
        INSERTED_AUTHOR_ID_VALUE)).isNull();
    Author exceptedAuthor = authorTestDataComponent.getPushkinAlexSergAuthor();
    assertThat(exceptedAuthor.getId()).isNull();
    exceptedAuthor = authorRepository.save(exceptedAuthor);
    assertThat(exceptedAuthor.getId())
        .isNotNull()
        .isEqualTo(INSERTED_AUTHOR_ID_VALUE);
    Author actualAuthor = testEntityManager.find(Author.class,
        INSERTED_AUTHOR_ID_VALUE);
    assertThat(actualAuthor)
        .isNotNull()
        .isEqualTo(exceptedAuthor);
  }

  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeThrowDataIntegrityViolationExceptionByAuthorNaturalKey() {
    Author exceptedAuthor = authorTestDataComponent.getBulychevKirAuthor();
    exceptedAuthor.setId(null);
    assertThatCode(() -> authorRepository.save(exceptedAuthor))
        .isNotNull()
        .isInstanceOf(DataIntegrityViolationException.class)
        .hasCauseInstanceOf(ConstraintViolationException.class);
  }

  @Test
  void shouldBeThrowDataIntegrityConstraintViolationByAuthorNaturalKey() {
    Author expectedAuthor = authorTestDataComponent.getUncompletedAuthorForInsertOperation();
    assertThatCode(() -> authorRepository.save(expectedAuthor))
        .isNotNull()
        .isInstanceOf(DataIntegrityViolationException.class)
        .hasCauseInstanceOf(ConstraintViolationException.class);
  }
  //endregion

  //region read
  @Test
  void shouldBeGetAllInsertedAuthors() {
    assertThat(authorRepository.findAll())
        .isNotNull()
        .isNotEmpty()
        .isEqualTo(authorTestDataComponent.getAllExistingAuthors());
  }
  //endregion

  //region update
  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeUpdateInsertedAuthor() {
    assertThat(testEntityManager.find(Author.class,
        INSERTED_AUTHOR_ID_VALUE)).isNull();
    Author exceptedAuthor = authorTestDataComponent.getMakiseKurisuAuthor();
    assertThat(exceptedAuthor.getId()).isNull();
    exceptedAuthor = authorRepository.save(exceptedAuthor);
    assertThat(exceptedAuthor.getId())
        .isNotNull()
        .isEqualTo(INSERTED_AUTHOR_ID_VALUE);
    exceptedAuthor.setSurname(MAKISE_KURISU_MARRIED_SURNAME);
    exceptedAuthor = authorRepository.save(exceptedAuthor);
    assertThat(exceptedAuthor.getId())
        .isNotNull()
        .isEqualTo(INSERTED_AUTHOR_ID_VALUE);
    Author actualAuthor = testEntityManager
        .find(Author.class,
            INSERTED_AUTHOR_ID_VALUE);
    assertThat(actualAuthor)
        .isNotNull()
        .isEqualTo(exceptedAuthor);
  }
  //endregion

  //region delete
  @Test
  void shouldBeDeleteInsertedAuthor() {
    Author exceptedAuthor = authorTestDataComponent.getTolstoyLevNikAuthor();
    Optional<Author> actualAuthorOptional = authorRepository
        .findById(EXISTING_AUTHOR_ID_VALUE);
    assertThat(actualAuthorOptional)
        .isNotNull()
        .isPresent();
    Author actualAuthor = actualAuthorOptional.get();
    assertThat(actualAuthor)
        .isEqualTo(exceptedAuthor);
    authorRepository.deleteById(EXISTING_AUTHOR_ID_VALUE);
    assertThat(authorRepository.findById(EXISTING_AUTHOR_ID_VALUE))
        .isNotNull()
        .isNotPresent();
  }
  //endregion
}