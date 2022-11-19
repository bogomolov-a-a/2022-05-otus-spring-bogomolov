package ru.otus.group202205.homework.spring10.web.rest;

import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.group202205.homework.spring10.dto.GenreDto;
import ru.otus.group202205.homework.spring10.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring10.model.Genre;
import ru.otus.group202205.homework.spring10.service.GenreService;
import ru.otus.group202205.homework.spring10.service.mapper.GenreMapper;
import ru.otus.group202205.homework.spring10.testdata.GenreTestDataComponent;

@WebMvcTest(GenreController.class)
@Import(GenreTestDataComponent.class)
class GenreControllerTest {

  private static final ObjectWriter GENRE_DTO_OBJECT_WRITER;
  private static final ObjectWriter GENRE_DTO_LIST_OBJECT_WRITER;

  static {
    ObjectMapper mapper = new ObjectMapper();
    GENRE_DTO_OBJECT_WRITER = mapper
        .writer()
        .forType(GenreDto.class);
    GENRE_DTO_LIST_OBJECT_WRITER = mapper
        .writer()
        .forType(TypeFactory
            .defaultInstance()
            .constructCollectionType(List.class,
                GenreDto.class));
  }

  @Autowired
  private MockMvc mvc;
  @Autowired
  private GenreTestDataComponent genreTestDataComponent;
  @MockBean
  private GenreService genreService;
  @MockBean
  private GenreMapper genreMapper;

  @BeforeEach
  void init() {
    Mockito.reset(genreService);
    Mockito.reset(genreMapper);
    Mockito
        .doThrow(new LibraryGeneralException("No such entity with id null",
            new NoSuchElementException()))
        .when(genreService)
        .findById(null);
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
  }

  @Test
  void shouldBeReturnCode200GenreReadCardPageWithId() throws
      Exception {
    GenreDto genreDto = genreTestDataComponent.getNovellGenreDto();
    Mockito
        .doReturn(genreDto)
        .when(genreService)
        .findById(1L);
    mvc
        .perform(MockMvcRequestBuilders
            .get("/genres/1"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isOk())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .content()
            .json(GENRE_DTO_OBJECT_WRITER.writeValueAsString(genreDto)));
  }

  @Test
  void shouldBeReturnCode200ReadGenres() throws
      Exception {
    List<GenreDto> genreDtos = genreTestDataComponent
        .getAllExistingGenres()
        .stream()
        .map(genreMapper::toDto)
        .collect(Collectors.toList());
    Mockito
        .doReturn(genreDtos)
        .when(genreService)
        .findAll();
    mvc
        .perform(MockMvcRequestBuilders.get("/genres"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isOk())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .content()
            .json(GENRE_DTO_LIST_OBJECT_WRITER.writeValueAsString(genreDtos)));
  }

  @Test
  void shouldBeReturnCode302DeleteGenreRedirectToGenres() throws
      Exception {
    Mockito
        .doNothing()
        .when(genreService)
        .deleteById(1L);
    mvc
        .perform(MockMvcRequestBuilders
            .delete("/genres/1"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isFound())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/genres/page"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("genres",
                "error",
                "message",
                "entityClass"));
  }

  @Test
  void shouldBeReturnCode302CreateGenreRedirectToGenres() throws
      Exception {
    Genre mangeGenre = genreTestDataComponent.getMangaGenre();
    GenreDto mangaGenreDto = genreMapper.toDto(mangeGenre);
    Mockito
        .doAnswer(invocation -> {
          GenreDto genreDto = invocation.getArgument(0);
          genreDto.setId(3L);
          return genreDto;
        })
        .when(genreService)
        .saveOrUpdate(mangaGenreDto);
    mvc
        .perform(MockMvcRequestBuilders
            .post("/genres")
            .queryParam("name",
                mangeGenre.getName())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isFound())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/genres/page"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("genres",
                "error",
                "entityClass",
                "message"));
  }

  @Test
  void shouldBeReturnCode302UpdateGenreRedirectToGenres() throws
      Exception {
    Genre scienceFictionGenre = genreTestDataComponent.getScienceFictionGenre();
    scienceFictionGenre.setName("Fanfiction");
    GenreDto scienceFictionGenreDto = genreMapper.toDto(scienceFictionGenre);
    Mockito
        .doAnswer(invocation -> invocation.getArgument(0))
        .when(genreService)
        .saveOrUpdate(scienceFictionGenreDto);
    mvc
        .perform(MockMvcRequestBuilders
            .post("/genres")
            .queryParam("id",
                String.valueOf(scienceFictionGenre.getId()))
            .queryParam("name",
                scienceFictionGenre.getName())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isFound())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/genres/page"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("genres",
                "error",
                "message",
                "entityClass"));
  }

  @Test
  void shouldBeReturnCode400CreateOrUpdateGenreThrowDuplicateKeyException() throws
      Exception {
    Genre novellGenre = genreTestDataComponent.getNovellGenre();
    novellGenre.setId(null);
    GenreDto novellGenreDto = genreMapper.toDto(novellGenre);
    Mockito
        .doThrow(new LibraryGeneralException("Can't create genre",
            new DuplicateKeyException("unique constraint violation")))
        .when(genreService)
        .saveOrUpdate(novellGenreDto);
    mvc
        .perform(MockMvcRequestBuilders
            .post("/genres")
            .queryParam("name",
                novellGenre.getName())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isBadRequest())
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("genres",
                "message",
                "entityClass"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("error"));
  }

  @Test
  void shouldBeReturnCode400CreateOrUpdateGenreThrowBindException() throws
      Exception {
    Genre liteNovellGenre = genreTestDataComponent.getLiteNovellGenre();
    liteNovellGenre.setName(liteNovellGenre.getName() + "()");
    mvc
        .perform(MockMvcRequestBuilders
            .post("/genres")
            .queryParam("name",
                liteNovellGenre.getName())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isBadRequest())
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("genres",
                "message"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("error",
                "entityClass"));
  }

}