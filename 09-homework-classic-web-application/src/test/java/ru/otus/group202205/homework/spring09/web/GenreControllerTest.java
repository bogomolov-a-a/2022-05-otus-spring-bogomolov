package ru.otus.group202205.homework.spring09.web;

import static org.mockito.ArgumentMatchers.any;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import ru.otus.group202205.homework.spring09.dto.GenreDto;
import ru.otus.group202205.homework.spring09.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring09.model.Genre;
import ru.otus.group202205.homework.spring09.service.GenreService;
import ru.otus.group202205.homework.spring09.service.mapper.GenreMapper;
import ru.otus.group202205.homework.spring09.testdata.GenreTestDataComponent;
import ru.otus.group202205.homework.spring09.web.webutil.ActionMode;
import ru.otus.group202205.homework.spring09.web.webutil.CardService;

@WebMvcTest(GenreController.class)
@Import(GenreTestDataComponent.class)
class GenreControllerTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private GenreTestDataComponent genreTestDataComponent;
  @MockBean
  private CardService cardService;
  @MockBean
  private GenreService genreService;
  @MockBean
  private GenreMapper genreMapper;

  @BeforeEach
  void init() {
    Mockito.reset(cardService);
    Mockito.reset(genreService);
    Mockito.reset(genreMapper);
    Mockito
        .doAnswer(invocation -> {
          Model model = invocation.getArgument(0);
          ActionMode actionMode = invocation.getArgument(1);
          String entityName = ((String) model.getAttribute("entityClass")).toLowerCase(Locale.ROOT);
          switch (actionMode) {
            case READ: {
              model.addAttribute("pageTitle",
                  String.format("Read %s page",
                      entityName));
              break;
            }
            case CREATE: {
              model.addAttribute("pageTitle",
                  String.format("Add new %s",
                      entityName));
              break;
            }
            case UPDATE: {
              model.addAttribute("pageTitle",
                  String.format("Update %s page",
                      entityName));
            }

          }
          boolean isView = ActionMode.READ.equals(actionMode);
          boolean isUpdating = ActionMode.UPDATE.equals(actionMode);
          boolean isCreating = ActionMode.CREATE.equals(actionMode);
          model.addAttribute("viewing",
              isView);
          model.addAttribute("editing",
              isCreating || isUpdating);
          model.addAttribute("updating",
              isUpdating);
          String submitButtonCaption = "Create";
          if (isUpdating) {
            model.addAttribute("submitButtonCaption",
                "Update");
            return null;
          }
          boolean editing = Boolean.parseBoolean(String.valueOf(model.getAttribute("editing")));
          if (editing) {
            model.addAttribute("submitButtonCaption",
                "Create");
          }
          return null;
        })
        .when(cardService)
        .prepareCard(any(),
            any());
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
  void shouldBeReturnGenreCreateCardPage() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/genre/get-genre/card-to-create.html",
        GenreControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders
            .get("/genre")
            .queryParam("actionMode",
                "CREATE"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isOk())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(exceptedContent));
  }

  @Test
  void shouldBeReturnCode400GenreReadCardPageWithoutId() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/genre/get-genre/read-update-card-id-error.html",
        GenreControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders
            .get("/genre")
            .queryParam("actionMode",
                "READ"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isBadRequest())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist(("genre")))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists(("error")))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists(("entityClass")))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(exceptedContent));
  }

  @Test
  void shouldBeReturnCode400GenreUpdateCardPageWithoutId() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/genre/get-genre/read-update-card-id-error.html",
        GenreControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders
            .get("/genre")
            .queryParam("actionMode",
                "UPDATE"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isBadRequest())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("pageTitle",
                "Update genre page"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("genre"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("entityClass"))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(exceptedContent));
  }

  @Test
  void shouldBeReturnCode400GenreDeleteCardPageWithoutId() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/genre/get-genre/delete-card-not-allowed-error.html",
        GenreControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders
            .get("/genre")
            .queryParam("actionMode",
                "DELETE"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isBadRequest())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("genre"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("entityClass"))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(exceptedContent));
  }

  @Test
  void shouldBeReturnCode200GenreReadCardPageWithId() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/genre/get-genre/read-genre-200-id-1-read.html",
        GenreControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    GenreDto genreDto = genreTestDataComponent.getScienceFictionGenreDto();
    Mockito
        .doReturn(genreDto)
        .when(genreService)
        .findById(1L);
    mvc
        .perform(MockMvcRequestBuilders
            .get("/genre")
            .queryParam("id",
                "1")
            .queryParam("actionMode",
                "READ"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isOk())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("pageTitle",
                "Read genre page"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("genre",
                genreDto))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("entityClass"))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(exceptedContent));
  }

  @Test
  void shouldBeReturnCode200ReadGenres() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/genre/get-genre/read-genres-200.html",
        GenreControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
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
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("genres",
                genreDtos))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("entityClass"))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(exceptedContent));
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
            .post("/genres/delete")
            .queryParam("id",
                "1")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isFound())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/genres?message=Genre+with+id+1+deleted%21"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("genres"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("message",
                "Genre with id 1 deleted!"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("entityClass"));
  }

  @Test
  void shouldBeReturnCode302CreateGenreRedirectToGenres() throws
      Exception {
    Genre mangeGenre = genreTestDataComponent.getMangaGenre();
    GenreDto pushkinGenreDto = genreMapper.toDto(mangeGenre);
    Mockito
        .doAnswer(invocation -> {
          GenreDto genreDto = invocation.getArgument(0);
          genreDto.setId(3L);
          return genreDto;
        })
        .when(genreService)
        .saveOrUpdate(pushkinGenreDto);
    mvc
        .perform(MockMvcRequestBuilders
            .post("/genres")
            .queryParam("name",
                mangeGenre.getName())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isFound())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/genres?message=Genre+with+id+3+saved%21"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("genres"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("message",
                "Genre with id 3 saved!"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("entityClass"));
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
        .andExpect(MockMvcResultMatchers.redirectedUrl("/genres?message=Genre+with+id+2+saved%21"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("genres"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("message",
                "Genre with id 2 saved!"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("entityClass"));
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