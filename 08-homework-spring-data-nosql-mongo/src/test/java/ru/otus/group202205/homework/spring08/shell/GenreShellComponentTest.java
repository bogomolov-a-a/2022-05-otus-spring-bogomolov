package ru.otus.group202205.homework.spring08.shell;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

import com.mongodb.MongoException;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.shell.Shell;
import ru.otus.group202205.homework.spring08.dto.GenreDto;
import ru.otus.group202205.homework.spring08.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring08.model.Genre;
import ru.otus.group202205.homework.spring08.service.GenreService;
import ru.otus.group202205.homework.spring08.service.converter.GenreConverter;
import ru.otus.group202205.homework.spring08.service.mapper.GenreMapper;
import ru.otus.group202205.homework.spring08.testdata.GenreTestDataComponent;

@SpringBootTest(classes = {GenreShellComponent.class, GenreTestDataComponent.class, TestShellConfig.class})
class GenreShellComponentTest {

  private static final String INSERTED_GENRE_ID_VALUE = "3";
  private static final String EXISTING_GENRE_ID_VALUE = "1";
  private static final String MISSING_GENRE_ID_VALUE = "4";
  private static final String NOVELL_UPDATED_GENRE_NAME = "Ranobe";
  @Autowired
  private Shell shell;
  @Autowired
  private GenreTestDataComponent genreTestDataComponent;
  @MockBean
  private GenreService genreService;
  @MockBean
  private GenreMapper genreMapper;
  @MockBean
  private GenreConverter genreConverter;

  @BeforeEach
  void init() {
    Mockito.reset(genreMapper);
    Mockito
        .doAnswer(invocation -> {
          Genre genre = invocation.getArgument(0);
          GenreDto result = new GenreDto();
          result.setId(genre.getId());
          result.setName(genre.getName());
          return result;
        })
        .when(genreMapper)
        .toDto(any());
    Mockito
        .doAnswer(invocation -> {
          GenreDto genre = invocation.getArgument(0);
          Genre result = new Genre();
          result.setId(genre.getId());
          result.setName(genre.getName());
          return result;
        })
        .when(genreMapper)
        .toEntity(any());
    Mockito
        .doAnswer(invocation -> {
          GenreDto genre = invocation.getArgument(0);
          return String.format("Genre id: %s%sname: %s%s",
              genre.getId(),
              System.lineSeparator(),
              genre.getName(),
              System.lineSeparator());
        })
        .when(genreConverter)
        .convertGenre(any());
    Mockito
        .doAnswer(invocation -> {
          List<GenreDto> genres = invocation.getArgument(0);
          StringBuilder result = new StringBuilder("Genre list").append(System.lineSeparator());
          genres.forEach(genre -> result.append(genreConverter.convertGenre(genre)));
          return result.toString();
        })
        .when(genreConverter)
        .convertGenres(anyList());
  }

  //region create
  @Test
  void shouldBeInsertNewGenre() {
    Genre exceptedGenre = genreTestDataComponent.getMangaGenre();
    GenreDto exceptedGenreDto = genreMapper.toDto(exceptedGenre);
    Mockito
        .doAnswer(invocation -> {
          GenreDto result = invocation.getArgument(0);
          result.setId(INSERTED_GENRE_ID_VALUE);
          return result;
        })
        .when(genreService)
        .saveOrUpdate(exceptedGenreDto);
    String successCreatedGenreMessage = (String) shell.evaluate(() -> String.format("create-genre --name %s",
        exceptedGenre.getName()));
    assertThat(successCreatedGenreMessage)
        .isNotNull()
        .isEqualTo("Genre with id " + INSERTED_GENRE_ID_VALUE + " created!");
  }


  @Test
  void shouldBeThrowDuplicateKeyExceptionByGenreNaturalKey() {
    Genre exceptedGenre = genreTestDataComponent.getNovellGenre();
    assertThat(exceptedGenre.getId())
        .isNotNull()
        .isEqualTo(EXISTING_GENRE_ID_VALUE);
    exceptedGenre.setId(null);
    GenreDto exceptedGenreDto = genreMapper.toDto(exceptedGenre);
    Mockito
        .doThrow(new LibraryGeneralException("Can't insert or update genre",
            new DuplicateKeyException("unique key violation!")))
        .when(genreService)
        .saveOrUpdate(exceptedGenreDto);
    String naturalKeyViolationMessage = (String) shell.evaluate(() -> String.format("create-genre --name %s",
        exceptedGenre.getName()));
    assertThat(naturalKeyViolationMessage)
        .isNotNull()
        .isEqualTo("Can't insert or update genre Cause unique key violation!");
  }
  //endregion

  //region read
  @Test
  void shouldBeReadAllInsertedGenres() {
    List<GenreDto> genreDtos = genreTestDataComponent
        .getAllExistingGenres()
        .stream()
        .map(genreMapper::toDto)
        .collect(Collectors.toList());
    Mockito
        .doReturn(genreDtos)
        .when(genreService)
        .findAll();
    String actualReadGenresOutput = (String) shell.evaluate(() -> "read-genres");
    assertThat(actualReadGenresOutput)
        .isNotNull()
        .isEqualTo("Genre list" + System.lineSeparator() + "Genre id: 1" + System.lineSeparator() + "name: Novell" + System.lineSeparator() + "Genre id: 2"
            + System.lineSeparator() + "name: Science fiction" + System.lineSeparator());
  }

  @Test
  void shouldBeReadGenreById() {
    Genre exceptedGenre = genreTestDataComponent.getNovellGenre();
    Mockito
        .doReturn(genreMapper.toDto(exceptedGenre))
        .when(genreService)
        .findById(EXISTING_GENRE_ID_VALUE);
    String actualReadAuthorsOutput = (String) shell.evaluate(() -> String.format("read-genre --id %s",
        EXISTING_GENRE_ID_VALUE));
    assertThat(actualReadAuthorsOutput)
        .isNotNull()
        .isEqualTo("Genre id: 1" + System.lineSeparator() + "name: Novell" + System.lineSeparator());
  }

  @Test
  void shouldBeThrowMongoExceptionByGetGenreByIdFour() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't read all genre by id " + MISSING_GENRE_ID_VALUE,
            new MongoException("table or view not exists")))
        .when(genreService)
        .findById(MISSING_GENRE_ID_VALUE);
    String actualReadGenreErrorMessage = (String) shell.evaluate(() -> String.format("read-genre --id %s",
        MISSING_GENRE_ID_VALUE));
    assertThat(actualReadGenreErrorMessage)
        .isNotNull()
        .isEqualTo("Can't read all genre by id " + MISSING_GENRE_ID_VALUE + " Cause table or view not exists");
  }

  @Test
  void shouldBeThrowMongoExceptionByGetAllGenres() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't read all genres",
            new MongoException("table or view not exists")))
        .when(genreService)
        .findAll();
    String actualReadGenreErrorMessage = (String) shell.evaluate(() -> "read-genres");
    assertThat(actualReadGenreErrorMessage)
        .isNotNull()
        .isEqualTo("Can't read all genres Cause table or view not exists");
  }
  //endregion

  //region update
  @Test
  void shouldBeUpdateGenre() {
    Genre exceptedGenre = genreTestDataComponent.getNovellGenre();
    assertThat(exceptedGenre.getId())
        .isNotNull()
        .isEqualTo(EXISTING_GENRE_ID_VALUE);
    GenreDto exceptedGenreDto = genreMapper.toDto(exceptedGenre);
    Mockito
        .doAnswer(invocation -> {
          GenreDto result = invocation.getArgument(0);
          result.setName(NOVELL_UPDATED_GENRE_NAME);
          return result;
        })
        .when(genreService)
        .saveOrUpdate(exceptedGenreDto);
    String actualUpdateErrorMessage = (String) shell.evaluate(() -> String.format("update-genre --id %s --name %s",
        exceptedGenre.getId(),
        NOVELL_UPDATED_GENRE_NAME));
    assertThat(actualUpdateErrorMessage)
        .isNotNull()
        .isEqualTo("Genre with id " + EXISTING_GENRE_ID_VALUE + " updated!");
  }

  @Test
  void shouldBeThrowMongoExceptionInUpdateGenre() {
    Genre exceptedGenre = new Genre();
    exceptedGenre.setId(MISSING_GENRE_ID_VALUE);
    exceptedGenre.setName(NOVELL_UPDATED_GENRE_NAME);
    GenreDto exceptedGenreDto = genreMapper.toDto(exceptedGenre);
    Mockito
        .doThrow(new LibraryGeneralException("Can't update genre",
            new MongoException("Not found author with id " + MISSING_GENRE_ID_VALUE)))
        .when(genreService)
        .saveOrUpdate(exceptedGenreDto);
    String actualUpdateErrorMessage = (String) shell.evaluate(() -> String.format("update-genre --id %s --name %s",
        exceptedGenre.getId(),
        NOVELL_UPDATED_GENRE_NAME));
    assertThat(actualUpdateErrorMessage)
        .isNotNull()
        .isEqualTo("Can't update genre Cause Not found author with id " + MISSING_GENRE_ID_VALUE);
  }
  //endregion

  //region delete
  @Test
  void shouldBeDeleteExistsGenre() {
    Mockito
        .doNothing()
        .when(genreService)
        .deleteById(EXISTING_GENRE_ID_VALUE);
    String successDeletedGenreMessage = (String) shell.evaluate(() -> String.format("delete-genre --id %s",
        EXISTING_GENRE_ID_VALUE));
    assertThat(successDeletedGenreMessage)
        .isNotNull()
        .isEqualTo("Genre with id " + EXISTING_GENRE_ID_VALUE + " deleted!");
  }

  @Test
  void shouldBeThrownMongoExceptionInDeleteGenreByIde() {
    Mockito
        .doThrow(new LibraryGeneralException("Can't delete author",
            new MongoException("Not found author with id " + MISSING_GENRE_ID_VALUE)))
        .when(genreService)
        .deleteById(MISSING_GENRE_ID_VALUE);
    String successDeletedGenreMessage = (String) shell.evaluate(() -> String.format("delete-genre --id %s",
        MISSING_GENRE_ID_VALUE));
    assertThat(successDeletedGenreMessage)
        .isNotNull()
        .isEqualTo("Can't delete author Cause Not found author with id " + MISSING_GENRE_ID_VALUE);
  }
  //endregion

}