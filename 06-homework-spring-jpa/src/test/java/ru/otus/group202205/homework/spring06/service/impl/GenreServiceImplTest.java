package ru.otus.group202205.homework.spring06.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.group202205.homework.spring06.dao.GenreRepository;
import ru.otus.group202205.homework.spring06.dto.GenreDto;
import ru.otus.group202205.homework.spring06.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring06.model.Genre;
import ru.otus.group202205.homework.spring06.service.GenreService;
import ru.otus.group202205.homework.spring06.service.mapper.GenreMapper;
import ru.otus.group202205.homework.spring06.service.mapper.impl.GenreMapperImpl;
import ru.otus.group202205.homework.spring06.testdata.GenreTestDataComponent;

@SpringBootTest(classes = {GenreServiceImpl.class, GenreTestDataComponent.class, GenreMapperImpl.class})
class GenreServiceImplTest {

  private static final Long INSERTED_GENRE_ID_VALUE = 3L;
  private static final Long EXISTING_GENRE_ID_VALUE = 2L;
  private static final Long MISSING_GENRE_ID_VALUE = 4L;
  private static final String LITE_NOVELL_UPDATED_GENRE_NAME = "Ranobe";

  @Autowired
  private GenreService genreService;
  @Autowired
  private GenreTestDataComponent genreTestDataComponent;
  @Autowired
  private GenreMapper genreMapper;
  @MockBean
  private GenreRepository genreRepositoryJpa;


  //region create
  @Test
  void shouldBeInsertNewGenre() {
    Genre exceptedGenre = genreTestDataComponent.getMangaGenre();
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          Genre result = ((Genre) args[0]);
          result.setId(INSERTED_GENRE_ID_VALUE);
          return result;
        })
        .when(genreRepositoryJpa)
        .saveOrUpdate(exceptedGenre);
    GenreDto exceptedGenreDto = genreMapper.toDto(exceptedGenre);
    assertThat(exceptedGenreDto.getId()).isNull();
    GenreDto actualGenreDto = genreService.saveOrUpdate(exceptedGenreDto);
    assertThat(exceptedGenreDto.getId()).isNull();
    exceptedGenreDto.setId(INSERTED_GENRE_ID_VALUE);
    assertThat(actualGenreDto)
        .isNotNull()
        .isEqualTo(exceptedGenreDto);
  }

  @Test
  void shouldBeThrowPersistenceExceptionByGenreNaturalKey() {
    Genre exceptedGenre = genreTestDataComponent.getNovellGenre();
    Mockito
        .doThrow(PersistenceException.class)
        .when(genreRepositoryJpa)
        .saveOrUpdate(exceptedGenre);
    assertThatCode(() -> genreService.saveOrUpdate(genreMapper.toDto(exceptedGenre)))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(PersistenceException.class);
  }
  //endregion

  //region read
  @Test
  void shouldBeGetAllInsertedGenres() {
    List<Genre> allGenres = genreTestDataComponent.getAllExistingGenres();
    List<GenreDto> allGenreDtos = allGenres
        .stream()
        .map(genreMapper::toDto)
        .collect(Collectors.toList());
    Mockito
        .doReturn(allGenres)
        .when(genreRepositoryJpa)
        .findAll();
    assertThat(genreService.findAll())
        .isNotNull()
        .isNotEmpty()
        .isEqualTo(allGenreDtos);
  }

  @Test
  void shouldBeThrowPersistenceExceptionInGetAllGenres() {
    Mockito
        .doThrow(PersistenceException.class)
        .when(genreRepositoryJpa)
        .findAll();
    assertThatCode(() -> genreService.findAll())
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(PersistenceException.class);
  }
  //endregion

  //region update
  @Test
  void shouldBeUpdateInsertedGenreName() {
    Genre exceptedInsertedGenre = genreTestDataComponent.getLiteNovellGenre();
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          Genre result = ((Genre) args[0]);
          result.setId(INSERTED_GENRE_ID_VALUE);
          return result;
        })
        .when(genreRepositoryJpa)
        .saveOrUpdate(exceptedInsertedGenre);
    GenreDto exceptedInsertedGenreDto = genreMapper.toDto(exceptedInsertedGenre);
    assertThat(exceptedInsertedGenreDto.getId()).isNull();
    GenreDto actualInsertedAuthorDto = genreService.saveOrUpdate(exceptedInsertedGenreDto);
    assertThat(exceptedInsertedGenreDto.getId()).isNull();
    assertThat(actualInsertedAuthorDto.getId())
        .isNotNull()
        .isEqualTo(INSERTED_GENRE_ID_VALUE);
    Genre updatingGenre = new Genre();
    updatingGenre.setId(INSERTED_GENRE_ID_VALUE);
    updatingGenre.setName(LITE_NOVELL_UPDATED_GENRE_NAME);
    Mockito
        .doAnswer(invocation -> {
          Object[] args = invocation.getArguments();
          exceptedInsertedGenre.setId(INSERTED_GENRE_ID_VALUE);
          exceptedInsertedGenre.setName(((Genre) args[0]).getName());
          return exceptedInsertedGenre;
        })
        .when(genreRepositoryJpa)
        .saveOrUpdate(updatingGenre);
    GenreDto updatingGenreDto = genreMapper.toDto(updatingGenre);
    GenreDto actualUpdatedDto = genreService.saveOrUpdate(updatingGenreDto);
    assertThat(actualUpdatedDto.getId())
        .isNotNull()
        .isEqualTo(INSERTED_GENRE_ID_VALUE);
    Mockito.reset(genreRepositoryJpa);
    Mockito
        .doReturn(Optional.of(exceptedInsertedGenre))
        .when(genreRepositoryJpa)
        .findById(INSERTED_GENRE_ID_VALUE);
    GenreDto exceptedUpdatedGenreDto = genreMapper.toDto(exceptedInsertedGenre);
    GenreDto actualGenreDto = genreService.findById(INSERTED_GENRE_ID_VALUE);
    assertThat(actualGenreDto)
        .isNotNull()
        .isEqualTo(exceptedUpdatedGenreDto);
  }

  @Test
  void shouldBePersistenceExceptionInUpdateGenre() {
    Genre emptyGenre = new Genre();
    Mockito
        .doThrow(PersistenceException.class)
        .when(genreRepositoryJpa)
        .saveOrUpdate(emptyGenre);
    assertThatCode(() -> genreService.saveOrUpdate(genreMapper.toDto(emptyGenre)))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(PersistenceException.class);
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
        .doReturn(Optional.of(exceptedGenre))
        .when(genreRepositoryJpa)
        .findById(EXISTING_GENRE_ID_VALUE);
    GenreDto actualGenreDto = genreService.findById(EXISTING_GENRE_ID_VALUE);
    GenreDto exceptedGenreDto = genreMapper.toDto(exceptedGenre);
    assertThat(actualGenreDto).isEqualTo(exceptedGenreDto);
    Mockito
        .doNothing()
        .when(genreRepositoryJpa)
        .deleteById(EXISTING_GENRE_ID_VALUE);
    genreService.deleteById(EXISTING_GENRE_ID_VALUE);
    Mockito.reset(genreRepositoryJpa);
    Mockito
        .doReturn(Optional.empty())
        .when(genreRepositoryJpa)
        .findById(EXISTING_GENRE_ID_VALUE);
    assertThatCode(() -> genreService.findById(EXISTING_GENRE_ID_VALUE))
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(NoSuchElementException.class);
  }


  @Test
  void shouldBePersistenceExceptionInDeleteGenreById() {
    Mockito
        .doThrow(PersistenceException.class)
        .when(genreRepositoryJpa)
        .deleteById(MISSING_GENRE_ID_VALUE);
    assertThatCode(() -> genreService.deleteById(MISSING_GENRE_ID_VALUE))
        .isNotNull()
        .isInstanceOf(LibraryGeneralException.class)
        .hasCauseInstanceOf(PersistenceException.class);
  }

  //endregion
  @Test
  void shouldBeAllPublicMethodsMarkedTransactional() {
    TypeResolver resolver = new TypeResolver();
    ResolvedType resolvedType = resolver.resolve(GenreServiceImpl.class);
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