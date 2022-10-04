package ru.otus.group202205.homework.spring05.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.group202205.homework.spring05.dao.AuthorDao;
import ru.otus.group202205.homework.spring05.model.Author;
import ru.otus.group202205.homework.spring05.testdata.AuthorTestDataComponent;

@JdbcTest
@Import({AuthorDaoJdbc.class, AuthorTestDataComponent.class})
class AuthorDaoJdbcTest {

  private static final Long INSERTED_AUTHOR_ID_VALUE = 3L;
  private static final Long EXISTING_AUTHOR_ID_VALUE = 2L;
  private static final String MAKISE_KURISU_MARRIED_SURNAME = "Okabe";
  @Autowired
  private AuthorDao authorDao;

  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;

  //region create
  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeInsertNewAuthor() {
    Author author = authorTestDataComponent.getMakiseKurisuAuthor();
    authorDao.insert(author);
    assertThat(author.getId())
        .isNotNull()
        .isEqualTo(INSERTED_AUTHOR_ID_VALUE);
  }

  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeInsertNewDeadAuthor() {
    Author author = authorTestDataComponent.getPushkinAlexSergAuthor();
    authorDao.insert(author);
    assertThat(author.getId())
        .isNotNull()
        .isEqualTo(INSERTED_AUTHOR_ID_VALUE);
  }

  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeThrowDuplicateKeyExceptionByAuthorNaturalKey() {
    Author exceptedAuthor = authorTestDataComponent.getBulychevKirAuthor();
    exceptedAuthor.setId(null);
    assertThatCode(() -> authorDao.insert(exceptedAuthor))
        .isNotNull()
        .isInstanceOf(DuplicateKeyException.class);
  }

  @Test
  void shouldBeThrowDataIntegrityViolationExceptionByAuthorNaturalKey() {
    Author expectedAuthor = authorTestDataComponent.getUncompletedAuthorForInsertOperation();
    assertThatCode(() -> authorDao.insert(expectedAuthor))
        .isNotNull()
        .isInstanceOf(DataIntegrityViolationException.class);
  }
  //endregion

  //region read
  @Test
  @Transactional(readOnly = true)
  void shouldBeGetAllInsertedAuthors() {
    assertThat(authorDao.getAll())
        .isNotNull()
        .isNotEmpty()
        .isEqualTo(authorTestDataComponent.getAllExistingAuthors());
  }
  //endregion

  //region update
  @Test
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  void shouldBeUpdateInsertedAuthor() {
    Author exceptedAuthor = authorTestDataComponent.getMakiseKurisuAuthor();
    authorDao.insert(exceptedAuthor);
    Long exceptedAuthorId = exceptedAuthor.getId();
    assertThat(exceptedAuthorId)
        .isNotNull()
        .isEqualTo(INSERTED_AUTHOR_ID_VALUE);
    exceptedAuthor.setSurname(MAKISE_KURISU_MARRIED_SURNAME);
    authorDao.update(exceptedAuthor);
    assertThat(exceptedAuthor.getId())
        .isNotNull()
        .isEqualTo(exceptedAuthorId);
    Author actualAuthor = authorDao.getById(exceptedAuthorId);
    assertThat(actualAuthor)
        .isNotNull()
        .isEqualTo(exceptedAuthor);
  }
  //endregion

  //region delete
  @Test
  void shouldBeDeleteInsertedAuthor() {
    Author exceptedAuthor = authorTestDataComponent.getTolstoyLevNikAuthor();
    assertThat(exceptedAuthor.getId())
        .isNotNull()
        .isEqualTo(EXISTING_AUTHOR_ID_VALUE);
    Long exceptedAuthorId = exceptedAuthor.getId();
    Author actualAuthor = authorDao.getById(exceptedAuthorId);
    assertThat(actualAuthor)
        .usingRecursiveComparison()
        .isEqualTo(exceptedAuthor);
    authorDao.deleteById(exceptedAuthorId);
    assertThatCode(() -> authorDao.getById(exceptedAuthorId)).isInstanceOf(EmptyResultDataAccessException.class);
  }
  //endregion
}