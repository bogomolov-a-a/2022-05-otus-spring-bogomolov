package ru.otus.group202205.homework.spring05.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.group202205.homework.spring05.dao.impl.AuthorDaoJdbc;
import ru.otus.group202205.homework.spring05.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring05.model.Author;
import ru.otus.group202205.homework.spring05.service.AuthorService;
import ru.otus.group202205.homework.spring05.testdata.AuthorTestDataComponent;

@SpringBootTest(classes = {AuthorServiceImpl.class, AuthorTestDataComponent.class})
class AuthorServiceImplTest {

  private static final Long INSERTED_AUTHOR_ID_VALUE = 3L;
  private static final Long MISSING_AUTHOR_ID_VALUE = 4L;
  private static final Long EXITING_AUTHOR_ID_VALUE = 2L;
  private static final String MAKISE_KURISU_MARRIED_SURNAME = "Okabe";
  private static final String MAKISE_KURISU_UPDATED_NAME = "Christina";
  private static final String MAKISE_KURISU_NICK_NAME = "KurigohanAndKamehameha";
  @Autowired
  private AuthorService authorService;

  @MockBean
  private AuthorDaoJdbc authorDaoJdbc;

  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;

  //region create
  @Test
  void shouldBeInsertNewAuthor() {
    Author actualAuthor = authorTestDataComponent.getMakiseKurisuAuthor();
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          ((Author) args[0]).setId(INSERTED_AUTHOR_ID_VALUE);
          return null;
        })
        .when(authorDaoJdbc)
        .insert(actualAuthor);
    authorService.insert(actualAuthor);
    Author exceptedAuthor = authorTestDataComponent.getMakiseKurisuAuthor();
    exceptedAuthor.setId(INSERTED_AUTHOR_ID_VALUE);
    assertThat(actualAuthor)
        .isNotNull()
        .usingRecursiveComparison()
        .isEqualTo(exceptedAuthor);
  }

  @Test
  void shouldBeInsertNewDeadAuthor() {
    Author actualAuthor = authorTestDataComponent.getPushkinAlexSergAuthor();
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          ((Author) args[0]).setId(INSERTED_AUTHOR_ID_VALUE);
          return null;
        })
        .when(authorDaoJdbc)
        .insert(actualAuthor);
    authorService.insert(actualAuthor);
    Author exceptedAuthor = authorTestDataComponent.getPushkinAlexSergAuthor();
    exceptedAuthor.setId(INSERTED_AUTHOR_ID_VALUE);
    assertThat(actualAuthor)
        .isNotNull()
        .usingRecursiveComparison()
        .isEqualTo(exceptedAuthor);
  }

  @Test
  void shouldBeThrowDuplicateKeyExceptionByAuthorNaturalKey() {
    Author exceptedAuthor = authorTestDataComponent.getBulychevKirAuthor();
    Mockito
        .doThrow(DuplicateKeyException.class)
        .when(authorDaoJdbc)
        .insert(exceptedAuthor);
    assertThatCode(() -> authorService.insert(exceptedAuthor))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(DuplicateKeyException.class);
  }

  @Test
  void shouldBeThrowDataIntegrityViolationExceptionByAuthorNaturalKey() {
    Author expectedAuthor = authorTestDataComponent.getUncompletedAuthorForInsertOperation();
    Mockito
        .doThrow(DataIntegrityViolationException.class)
        .when(authorDaoJdbc)
        .insert(expectedAuthor);
    assertThatCode(() -> authorService.insert(expectedAuthor))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(DataIntegrityViolationException.class);
  }

  //endregion

  //region read
  @Test
  void shouldBeGetAllInsertedAuthors() {
    List<Author> allExistingAuthors = authorTestDataComponent.getAllExistingAuthors();
    Mockito
        .doReturn(allExistingAuthors)
        .when(authorDaoJdbc)
        .getAll();
    assertThat(authorService.getAll())
        .isNotNull()
        .isNotEmpty()
        .isEqualTo(allExistingAuthors);
  }

  @Test
  void shouldBeThrowDataAccessExceptionByGetAllAuthors() {
    Mockito
        .doThrow(DataRetrievalFailureException.class)
        .when(authorDaoJdbc)
        .getAll();
    assertThatCode(() -> authorService.getAll())
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(DataRetrievalFailureException.class);
  }
  //endregion

  //region update
  @Test
  void shouldBeUpdateInsertedAuthorName() {
    Author exceptedInsertedAuthor = authorTestDataComponent.getMakiseKurisuAuthor();
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          ((Author) args[0]).setId(INSERTED_AUTHOR_ID_VALUE);
          return null;
        })
        .when(authorDaoJdbc)
        .insert(exceptedInsertedAuthor);
    authorService.insert(exceptedInsertedAuthor);
    Long exceptedAuthorId = exceptedInsertedAuthor.getId();
    assertThat(exceptedAuthorId)
        .isNotNull()
        .isEqualTo(INSERTED_AUTHOR_ID_VALUE);
    Author exceptedUpdatedAuthor = new Author();
    exceptedUpdatedAuthor.setId(exceptedAuthorId);
    exceptedUpdatedAuthor.setName(MAKISE_KURISU_UPDATED_NAME);
    Mockito
        .doNothing()
        .when(authorDaoJdbc)
        .update(exceptedUpdatedAuthor);
    Mockito
        .doReturn(exceptedInsertedAuthor)
        .when(authorDaoJdbc)
        .getById(INSERTED_AUTHOR_ID_VALUE);
    authorService.update(exceptedUpdatedAuthor);
    assertThat(exceptedUpdatedAuthor.getId())
        .isNotNull()
        .isEqualTo(INSERTED_AUTHOR_ID_VALUE);
    Mockito.reset(authorDaoJdbc);
    Mockito
        .doReturn(exceptedUpdatedAuthor)
        .when(authorDaoJdbc)
        .getById(INSERTED_AUTHOR_ID_VALUE);
    Author actualAuthor = authorService.getById(INSERTED_AUTHOR_ID_VALUE);
    assertThat(actualAuthor)
        .isNotNull()
        .isEqualTo(exceptedUpdatedAuthor);
  }

  @Test
  void shouldBeUpdateInsertedAuthorSurname() {
    Author exceptedInsertedAuthor = authorTestDataComponent.getMakiseKurisuAuthor();
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          ((Author) args[0]).setId(INSERTED_AUTHOR_ID_VALUE);
          return null;
        })
        .when(authorDaoJdbc)
        .insert(exceptedInsertedAuthor);
    authorService.insert(exceptedInsertedAuthor);
    Long exceptedAuthorId = exceptedInsertedAuthor.getId();
    assertThat(exceptedAuthorId)
        .isNotNull()
        .isEqualTo(INSERTED_AUTHOR_ID_VALUE);
    Author exceptedUpdatedAuthor = new Author();
    exceptedUpdatedAuthor.setId(exceptedAuthorId);
    exceptedUpdatedAuthor.setSurname(MAKISE_KURISU_MARRIED_SURNAME);
    Mockito
        .doNothing()
        .when(authorDaoJdbc)
        .update(exceptedUpdatedAuthor);
    Mockito
        .doReturn(exceptedInsertedAuthor)
        .when(authorDaoJdbc)
        .getById(INSERTED_AUTHOR_ID_VALUE);
    authorService.update(exceptedUpdatedAuthor);
    assertThat(exceptedUpdatedAuthor.getId())
        .isNotNull()
        .isEqualTo(INSERTED_AUTHOR_ID_VALUE);
    Mockito.reset(authorDaoJdbc);
    Mockito
        .doReturn(exceptedUpdatedAuthor)
        .when(authorDaoJdbc)
        .getById(INSERTED_AUTHOR_ID_VALUE);
    Author actualAuthor = authorService.getById(INSERTED_AUTHOR_ID_VALUE);
    assertThat(actualAuthor)
        .isNotNull()
        .isEqualTo(exceptedUpdatedAuthor);
  }

  @Test
  void shouldBeUpdateInsertedAuthorPatronymic() {
    Author exceptedInsertedAuthor = authorTestDataComponent.getMakiseKurisuAuthor();
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          ((Author) args[0]).setId(INSERTED_AUTHOR_ID_VALUE);
          return null;
        })
        .when(authorDaoJdbc)
        .insert(exceptedInsertedAuthor);
    authorService.insert(exceptedInsertedAuthor);
    Long exceptedAuthorId = exceptedInsertedAuthor.getId();
    assertThat(exceptedAuthorId)
        .isNotNull()
        .isEqualTo(INSERTED_AUTHOR_ID_VALUE);
    Author exceptedUpdatedAuthor = new Author();
    exceptedUpdatedAuthor.setId(exceptedAuthorId);
    exceptedUpdatedAuthor.setPatronymic(MAKISE_KURISU_NICK_NAME);
    Mockito
        .doNothing()
        .when(authorDaoJdbc)
        .update(exceptedUpdatedAuthor);
    Mockito
        .doReturn(exceptedInsertedAuthor)
        .when(authorDaoJdbc)
        .getById(INSERTED_AUTHOR_ID_VALUE);
    authorService.update(exceptedUpdatedAuthor);
    assertThat(exceptedUpdatedAuthor.getId())
        .isNotNull()
        .isEqualTo(INSERTED_AUTHOR_ID_VALUE);
    Mockito.reset(authorDaoJdbc);
    Mockito
        .doReturn(exceptedUpdatedAuthor)
        .when(authorDaoJdbc)
        .getById(INSERTED_AUTHOR_ID_VALUE);
    Author actualAuthor = authorService.getById(INSERTED_AUTHOR_ID_VALUE);
    assertThat(actualAuthor)
        .isNotNull()
        .isEqualTo(exceptedUpdatedAuthor);
  }

  @Test
  void shouldBeNullPointerExceptionInUpdateAuthor() {
    Author emptyAuthor = new Author();
    Mockito
        .doThrow(NullPointerException.class)
        .when(authorDaoJdbc)
        .update(emptyAuthor);
    assertThatCode(() -> authorService.update(emptyAuthor))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(NullPointerException.class);
  }
  //endregion

  //region delete
  @Test
  void shouldBeDeleteInsertedAuthor() {
    Author exceptedAuthor = authorTestDataComponent.getTolstoyLevNikAuthor();
    Long exceptedAuthorId = exceptedAuthor.getId();
    assertThat(exceptedAuthorId)
        .isNotNull()
        .isEqualTo(EXITING_AUTHOR_ID_VALUE);
    Mockito
        .doReturn(exceptedAuthor)
        .when(authorDaoJdbc)
        .getById(exceptedAuthorId);
    Author actualAuthor = authorService.getById(exceptedAuthorId);
    assertThat(actualAuthor)
        .usingRecursiveComparison()
        .isEqualTo(exceptedAuthor);
    Mockito
        .doNothing()
        .when(authorDaoJdbc)
        .deleteById(exceptedAuthorId);
    authorService.deleteById(exceptedAuthorId);
    Mockito.reset(authorDaoJdbc);
    Mockito
        .doThrow(EmptyResultDataAccessException.class)
        .when(authorDaoJdbc)
        .getById(exceptedAuthorId);
    assertThatCode(() -> authorService.getById(exceptedAuthorId))
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(EmptyResultDataAccessException.class);
  }

  @Test
  void shouldBeDataRetrievalFailureExceptionInDeleteAuthorById() {
    Mockito
        .doThrow(DataRetrievalFailureException.class)
        .when(authorDaoJdbc)
        .deleteById(MISSING_AUTHOR_ID_VALUE);
    assertThatCode(() -> authorService.deleteById(MISSING_AUTHOR_ID_VALUE))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(DataRetrievalFailureException.class);
  }
  //endregion

}