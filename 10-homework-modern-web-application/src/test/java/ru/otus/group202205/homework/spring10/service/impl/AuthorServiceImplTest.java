package ru.otus.group202205.homework.spring10.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.RawMember;
import com.fasterxml.classmate.members.RawMethod;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.group202205.homework.spring10.dao.AuthorRepository;
import ru.otus.group202205.homework.spring10.dto.AuthorDto;
import ru.otus.group202205.homework.spring10.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring10.model.Author;
import ru.otus.group202205.homework.spring10.service.AuthorService;
import ru.otus.group202205.homework.spring10.service.mapper.AuthorMapper;
import ru.otus.group202205.homework.spring10.testdata.AuthorTestDataComponent;

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
  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;
  @MockBean
  private AuthorRepository authorRepositoryJpa;
  @MockBean
  private AuthorMapper authorMapper;

  @BeforeEach
  void init() {
    Mockito.reset(authorMapper);
    Mockito
        .doAnswer(invocation -> {
          Author author = invocation.getArgument(0);
          AuthorDto result = new AuthorDto();
          result.setId(author.getId());
          result.setName(author.getName());
          result.setSurname(author.getSurname());
          result.setPatronymic(author.getPatronymic());
          result.setBirthYear(author.getBirthYear());
          result.setDeathYear(author.getDeathYear());
          return result;
        })
        .when(authorMapper)
        .toDto(any());
    Mockito
        .doAnswer(invocation -> {
          AuthorDto authorDto = invocation.getArgument(0);
          Author result = new Author();
          result.setId(authorDto.getId());
          result.setName(authorDto.getName());
          result.setSurname(authorDto.getSurname());
          result.setPatronymic(authorDto.getPatronymic());
          result.setBirthYear(authorDto.getBirthYear());
          result.setDeathYear(authorDto.getDeathYear());
          return result;
        })
        .when(authorMapper)
        .toEntity(any());
  }

  //region create
  @Test
  void shouldBeInsertNewAuthor() {
    Author exceptedAuthor = authorTestDataComponent.getMakiseKurisuAuthor();
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          Author result = ((Author) args[0]);
          result.setId(INSERTED_AUTHOR_ID_VALUE);
          return result;
        })
        .when(authorRepositoryJpa)
        .save(exceptedAuthor);
    AuthorDto exceptedAuthorDto = authorMapper.toDto(exceptedAuthor);
    assertThat(exceptedAuthorDto.getId()).isNull();
    AuthorDto actualAuthorDto = authorService.saveOrUpdate(exceptedAuthorDto);
    assertThat(exceptedAuthorDto.getId()).isNull();
    exceptedAuthorDto.setId(INSERTED_AUTHOR_ID_VALUE);
    assertThat(actualAuthorDto)
        .isNotNull()
        .isEqualTo(exceptedAuthorDto);
  }

  @Test
  void shouldBeInsertNewDeadAuthor() {
    Author exceptedAuthor = authorTestDataComponent.getPushkinAlexSergAuthor();
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          Author result = ((Author) args[0]);
          result.setId(INSERTED_AUTHOR_ID_VALUE);
          return result;
        })
        .when(authorRepositoryJpa)
        .save(exceptedAuthor);
    AuthorDto exceptedAuthorDto = authorMapper.toDto(exceptedAuthor);
    assertThat(exceptedAuthorDto.getId()).isNull();
    AuthorDto actualAuthorDto = authorService.saveOrUpdate(exceptedAuthorDto);
    assertThat(exceptedAuthorDto.getId()).isNull();
    exceptedAuthorDto.setId(INSERTED_AUTHOR_ID_VALUE);
    assertThat(actualAuthorDto)
        .isNotNull()
        .isEqualTo(exceptedAuthorDto);
  }

  @Test
  void shouldBeThrowPersistenceDuplicateConstraintViolationByAuthorNaturalKey() {
    Author exceptedAuthor = authorTestDataComponent.getBulychevKirAuthor();
    Mockito
        .doThrow(new PersistenceException())
        .when(authorRepositoryJpa)
        .save(exceptedAuthor);
    assertThatCode(() -> authorService.saveOrUpdate(authorMapper.toDto(exceptedAuthor)))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(PersistenceException.class);
  }

  @Test
  void shouldBeThrowDataIntegrityViolationExceptionByAuthorNaturalKey() {
    Author exceptedAuthor = authorTestDataComponent.getUncompletedAuthorForInsertOperation();
    Mockito
        .doThrow(new PersistenceException())
        .when(authorRepositoryJpa)
        .save(exceptedAuthor);
    assertThatCode(() -> authorService.saveOrUpdate(authorMapper.toDto(exceptedAuthor)))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(PersistenceException.class);
  }

  //endregion

  //region read
  @Test
  void shouldBeGetAllInsertedAuthors() {
    List<Author> allExistingAuthors = authorTestDataComponent.getAllExistingAuthors();
    List<AuthorDto> allExistingAuthorDtos = allExistingAuthors
        .stream()
        .map(authorMapper::toDto)
        .collect(Collectors.toList());
    Mockito
        .doReturn(allExistingAuthors)
        .when(authorRepositoryJpa)
        .findAll();
    assertThat(authorService.findAll())
        .isNotNull()
        .isNotEmpty()
        .isEqualTo(allExistingAuthorDtos);
  }

  @Test
  void shouldBeThrowDataAccessExceptionByGetAllAuthors() {
    Mockito
        .doThrow(PersistenceException.class)
        .when(authorRepositoryJpa)
        .findAll();
    assertThatCode(() -> authorService.findAll())
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(PersistenceException.class);
  }
  //endregion

  //region update
  @Test
  void shouldBeUpdateInsertedAuthorName() {
    Author exceptedInsertedAuthor = authorTestDataComponent.getMakiseKurisuAuthor();
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          Author result = ((Author) args[0]);
          result.setId(INSERTED_AUTHOR_ID_VALUE);
          return result;
        })
        .when(authorRepositoryJpa)
        .save(exceptedInsertedAuthor);
    AuthorDto exceptedInsertedAuthorDto = authorMapper.toDto(exceptedInsertedAuthor);
    assertThat(exceptedInsertedAuthorDto.getId()).isNull();
    AuthorDto actualInsertedAuthorDto = authorService.saveOrUpdate(exceptedInsertedAuthorDto);
    assertThat(exceptedInsertedAuthorDto.getId()).isNull();
    Long exceptedAuthorId = actualInsertedAuthorDto.getId();
    assertThat(exceptedAuthorId)
        .isNotNull()
        .isEqualTo(INSERTED_AUTHOR_ID_VALUE);
    Author updatingAuthor = new Author();
    updatingAuthor.setId(exceptedAuthorId);
    updatingAuthor.setName(MAKISE_KURISU_UPDATED_NAME);
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          exceptedInsertedAuthor.setId(INSERTED_AUTHOR_ID_VALUE);
          exceptedInsertedAuthor.setName(((Author) args[0]).getName());
          return exceptedInsertedAuthor;
        })
        .when(authorRepositoryJpa)
        .save(updatingAuthor);
    AuthorDto updatingAuthorDto = authorMapper.toDto(updatingAuthor);
    AuthorDto actualUpdatedDto = authorService.saveOrUpdate(updatingAuthorDto);
    assertThat(actualUpdatedDto.getId())
        .isNotNull()
        .isEqualTo(INSERTED_AUTHOR_ID_VALUE);
    Mockito.reset(authorRepositoryJpa);
    Mockito
        .doReturn(Optional.of(exceptedInsertedAuthor))
        .when(authorRepositoryJpa)
        .findById(INSERTED_AUTHOR_ID_VALUE);
    AuthorDto exceptedUpdatedAuthorDto = authorMapper.toDto(exceptedInsertedAuthor);
    AuthorDto actualAuthorDto = authorService.findById(INSERTED_AUTHOR_ID_VALUE);
    assertThat(actualAuthorDto)
        .isNotNull()
        .isEqualTo(exceptedUpdatedAuthorDto);
  }

  @Test
  void shouldBeUpdateInsertedAuthorSurname() {
    Author exceptedInsertedAuthor = authorTestDataComponent.getMakiseKurisuAuthor();
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          Author result = ((Author) args[0]);
          result.setId(INSERTED_AUTHOR_ID_VALUE);
          return result;
        })
        .when(authorRepositoryJpa)
        .save(exceptedInsertedAuthor);
    AuthorDto exceptedInsertedAuthorDto = authorMapper.toDto(exceptedInsertedAuthor);
    assertThat(exceptedInsertedAuthorDto.getId()).isNull();
    AuthorDto actualInsertedAuthorDto = authorService.saveOrUpdate(exceptedInsertedAuthorDto);
    assertThat(exceptedInsertedAuthorDto.getId()).isNull();
    Long exceptedAuthorId = actualInsertedAuthorDto.getId();
    assertThat(exceptedAuthorId)
        .isNotNull()
        .isEqualTo(INSERTED_AUTHOR_ID_VALUE);
    Author updatingAuthor = new Author();
    updatingAuthor.setId(exceptedAuthorId);
    updatingAuthor.setSurname(MAKISE_KURISU_MARRIED_SURNAME);
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          exceptedInsertedAuthor.setId(INSERTED_AUTHOR_ID_VALUE);
          exceptedInsertedAuthor.setSurname(((Author) args[0]).getSurname());
          return exceptedInsertedAuthor;
        })
        .when(authorRepositoryJpa)
        .save(updatingAuthor);
    AuthorDto updatingAuthorDto = authorMapper.toDto(updatingAuthor);
    AuthorDto actualUpdatedDto = authorService.saveOrUpdate(updatingAuthorDto);
    assertThat(actualUpdatedDto.getId())
        .isNotNull()
        .isEqualTo(INSERTED_AUTHOR_ID_VALUE);
    Mockito.reset(authorRepositoryJpa);
    Mockito
        .doReturn(Optional.of(exceptedInsertedAuthor))
        .when(authorRepositoryJpa)
        .findById(INSERTED_AUTHOR_ID_VALUE);
    AuthorDto exceptedUpdatedAuthorDto = authorMapper.toDto(exceptedInsertedAuthor);
    AuthorDto actualAuthorDto = authorService.findById(INSERTED_AUTHOR_ID_VALUE);
    assertThat(actualAuthorDto)
        .isNotNull()
        .isEqualTo(exceptedUpdatedAuthorDto);
  }

  @Test
  void shouldBeUpdateInsertedAuthorPatronymic() {
    Author exceptedInsertedAuthor = authorTestDataComponent.getMakiseKurisuAuthor();
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          Author result = ((Author) args[0]);
          result.setId(INSERTED_AUTHOR_ID_VALUE);
          return result;
        })
        .when(authorRepositoryJpa)
        .save(exceptedInsertedAuthor);
    AuthorDto exceptedInsertedAuthorDto = authorMapper.toDto(exceptedInsertedAuthor);
    assertThat(exceptedInsertedAuthorDto.getId()).isNull();
    AuthorDto actualInsertedAuthorDto = authorService.saveOrUpdate(exceptedInsertedAuthorDto);
    assertThat(exceptedInsertedAuthorDto.getId()).isNull();
    Long exceptedAuthorId = actualInsertedAuthorDto.getId();
    assertThat(exceptedAuthorId)
        .isNotNull()
        .isEqualTo(INSERTED_AUTHOR_ID_VALUE);
    Author updatingAuthor = new Author();
    updatingAuthor.setId(exceptedAuthorId);
    updatingAuthor.setPatronymic(MAKISE_KURISU_NICK_NAME);
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          exceptedInsertedAuthor.setId(INSERTED_AUTHOR_ID_VALUE);
          exceptedInsertedAuthor.setPatronymic(((Author) args[0]).getPatronymic());
          return exceptedInsertedAuthor;
        })
        .when(authorRepositoryJpa)
        .save(updatingAuthor);
    AuthorDto updatingAuthorDto = authorMapper.toDto(updatingAuthor);
    AuthorDto actualUpdatedDto = authorService.saveOrUpdate(updatingAuthorDto);
    assertThat(actualUpdatedDto.getId())
        .isNotNull()
        .isEqualTo(INSERTED_AUTHOR_ID_VALUE);
    Mockito.reset(authorRepositoryJpa);
    Mockito
        .doReturn(Optional.of(exceptedInsertedAuthor))
        .when(authorRepositoryJpa)
        .findById(INSERTED_AUTHOR_ID_VALUE);
    AuthorDto exceptedUpdatedAuthorDto = authorMapper.toDto(exceptedInsertedAuthor);
    AuthorDto actualAuthorDto = authorService.findById(INSERTED_AUTHOR_ID_VALUE);
    assertThat(actualAuthorDto)
        .isNotNull()
        .isEqualTo(exceptedUpdatedAuthorDto);

  }

  @Test
  void shouldBePersistenceExceptionInUpdateAuthor() {
    Author emptyAuthor = new Author();
    Mockito
        .doThrow(PersistenceException.class)
        .when(authorRepositoryJpa)
        .save(emptyAuthor);
    assertThatCode(() -> authorService.saveOrUpdate(authorMapper.toDto(emptyAuthor)))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(PersistenceException.class);
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
        .doReturn(Optional.of(exceptedAuthor))
        .when(authorRepositoryJpa)
        .findById(exceptedAuthorId);
    AuthorDto actualAuthorDto = authorService.findById(exceptedAuthorId);
    AuthorDto exceptedAuthorDto = authorMapper.toDto(exceptedAuthor);
    assertThat(actualAuthorDto).isEqualTo(exceptedAuthorDto);
    Mockito
        .doNothing()
        .when(authorRepositoryJpa)
        .deleteById(exceptedAuthorId);
    authorService.deleteById(exceptedAuthorId);
    Mockito.reset(authorRepositoryJpa);
    Mockito
        .doReturn(Optional.empty())
        .when(authorRepositoryJpa)
        .findById(exceptedAuthorId);
    assertThatCode(() -> authorService.findById(exceptedAuthorId))
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(NoSuchElementException.class);
  }

  @Test
  void shouldBeDataPersistenceExceptionInDeleteAuthorById() {
    Mockito
        .doThrow(PersistenceException.class)
        .when(authorRepositoryJpa)
        .deleteById(MISSING_AUTHOR_ID_VALUE);
    assertThatCode(() -> authorService.deleteById(MISSING_AUTHOR_ID_VALUE))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(PersistenceException.class);
  }
  //endregion

  @Test
  void shouldBeAllPublicMethodsMarkedTransactional() {
    TypeResolver resolver = new TypeResolver();
    ResolvedType resolvedType = resolver.resolve(AuthorServiceImpl.class);
    List<RawMethod> methods = resolvedType.getMemberMethods();
    methods
        .stream()
        .filter(RawMember::isPublic)
        .forEach(method -> assertThat(Arrays
            .stream(method.getAnnotations())
            .map(Annotation::annotationType)
            .collect(Collectors.toList())
            .contains(Transactional.class)).isFalse());
  }

}