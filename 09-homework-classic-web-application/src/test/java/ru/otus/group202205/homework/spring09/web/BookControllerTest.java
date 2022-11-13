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
import ru.otus.group202205.homework.spring09.dto.AuthorDto;
import ru.otus.group202205.homework.spring09.dto.BookFullDto;
import ru.otus.group202205.homework.spring09.dto.GenreDto;
import ru.otus.group202205.homework.spring09.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring09.model.Author;
import ru.otus.group202205.homework.spring09.model.Book;
import ru.otus.group202205.homework.spring09.model.Genre;
import ru.otus.group202205.homework.spring09.service.AuthorService;
import ru.otus.group202205.homework.spring09.service.BookService;
import ru.otus.group202205.homework.spring09.service.GenreService;
import ru.otus.group202205.homework.spring09.service.mapper.AuthorMapper;
import ru.otus.group202205.homework.spring09.service.mapper.BookMapper;
import ru.otus.group202205.homework.spring09.service.mapper.GenreMapper;
import ru.otus.group202205.homework.spring09.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring09.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring09.testdata.GenreTestDataComponent;
import ru.otus.group202205.homework.spring09.web.webutil.ActionMode;
import ru.otus.group202205.homework.spring09.web.webutil.CardService;

@WebMvcTest(BookController.class)
@Import({BookTestDataComponent.class, AuthorTestDataComponent.class, GenreTestDataComponent.class})
class BookControllerTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private BookTestDataComponent bookTestDataComponent;
  @Autowired
  private AuthorTestDataComponent authorTestDataComponent;
  @Autowired
  private GenreTestDataComponent genreTestDataComponent;
  @MockBean
  private CardService cardService;
  @MockBean
  private BookService bookService;
  @MockBean
  private AuthorService authorService;
  @MockBean
  private GenreService genreService;
  @MockBean
  private BookMapper bookMapper;
  @MockBean
  private AuthorMapper authorMapper;
  @MockBean
  private GenreMapper genreMapper;


  @BeforeEach
  void init() {
    Mockito.reset(cardService);
    Mockito.reset(bookService);
    Mockito.reset(bookMapper);
    Mockito.reset(authorService);
    Mockito.reset(genreService);
    Mockito.reset(authorMapper);
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
        .when(bookService)
        .findById(null);
    Mockito
        .doAnswer(invocation -> {
          BookFullDto bookDto = invocation.getArgument(0);
          Book book = new Book();
          book.setId(bookDto.getId());
          book.setTitle(bookDto.getTitle());
          book.setIsbn(bookDto.getIsbn());
          AuthorDto authorDto = bookDto.getAuthor();
          if (authorDto != null) {
            Author author = new Author();
            author.setId(authorDto.getId());
            author.setSurname(authorDto.getSurname());
            author.setName(authorDto.getName());
            author.setPatronymic(authorDto.getPatronymic());
            author.setBirthYear(authorDto.getBirthYear());
            author.setDeathYear(authorDto.getDeathYear());
            book.setAuthor(author);
          }
          GenreDto genreDto = bookDto.getGenre();
          if (genreDto != null) {
            Genre genre = new Genre();
            genre.setId(genreDto.getId());
            genre.setName(genreDto.getName());
            book.setGenre(genre);
          }
          return book;
        })
        .when(bookMapper)
        .toEntityFromFull(any());
    Mockito
        .doAnswer(invocation -> {
          Book book = invocation.getArgument(0);
          BookFullDto bookDto = new BookFullDto();
          bookDto.setId(book.getId());
          bookDto.setTitle(book.getTitle());
          bookDto.setIsbn(book.getIsbn());
          Author author = book.getAuthor();
          if (author != null) {
            AuthorDto authorDto = new AuthorDto();
            authorDto.setId(author.getId());
            authorDto.setSurname(author.getSurname());
            authorDto.setName(author.getName());
            authorDto.setPatronymic(author.getPatronymic());
            authorDto.setBirthYear(author.getBirthYear());
            authorDto.setDeathYear(author.getDeathYear());
            bookDto.setAuthor(authorDto);
          }
          Genre genre = book.getGenre();
          if (genre != null) {
            GenreDto genreDto = new GenreDto();
            genreDto.setId(genre.getId());
            genreDto.setName(genre.getName());
            bookDto.setGenre(genreDto);
          }
          return bookDto;
        })
        .when(bookMapper)
        .toFullDto(any());
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
    List<AuthorDto> authorDtos = authorTestDataComponent
        .getAllExistingAuthors()
        .stream()
        .map(authorMapper::toDto)
        .collect(Collectors.toList());
    Mockito
        .doReturn(authorDtos)
        .when(authorService)
        .findAll();

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
    List<GenreDto> genreDtos = genreTestDataComponent
        .getAllExistingGenres()
        .stream()
        .map(genreMapper::toDto)
        .collect(Collectors.toList());
    Mockito
        .doAnswer(invocation -> {
          return genreDtos;
        })
        .when(genreService)
        .findAll();

  }

  @Test
  void shouldBeReturnBookCreateCardPage() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/book/get-book/card-to-create.html",
        BookControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders
            .get("/book")
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
  void shouldBeReturnCode400BookReadCardPageWithoutId() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/book/get-book/read-update-card-id-error.html",
        BookControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders
            .get("/book")
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
            .attributeDoesNotExist("book"))
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
  void shouldBeReturnCode400BookUpdateCardPageWithoutId() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/book/get-book/read-update-card-id-error.html",
        BookControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders
            .get("/book")
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
                "Update book page"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("book"))
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
  void shouldBeReturnCode400BookDeleteCardPageWithoutId() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/book/get-book/delete-card-not-allowed-error.html",
        BookControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders
            .get("/book")
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
            .attributeDoesNotExist("book"))
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
  void shouldBeReturnCode200BookReadCardPageWithId() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/book/get-book/read-book-200-id-1-read.html",
        BookControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    BookFullDto bookFullDto = bookTestDataComponent.getGirlFromEarthBookDto();
    Mockito
        .doReturn(bookFullDto)
        .when(bookService)
        .findById(1L);
    mvc
        .perform(MockMvcRequestBuilders
            .get("/book")
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
                "Read book page"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("book",
                bookFullDto))
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
  void shouldBeReturnCode200ReadBooks() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/book/get-book/read-books-200.html",
        BookControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    List<BookFullDto> bookDtos = bookTestDataComponent
        .getAllExistingBooks()
        .stream()
        .map(bookMapper::toFullDto)
        .collect(Collectors.toList());
    Mockito
        .doReturn(bookDtos)
        .when(bookService)
        .findAll();
    mvc
        .perform(MockMvcRequestBuilders.get("/books"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isOk())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("books",
                bookDtos))
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
  void shouldBeReturnCode302DeleteBookRedirectToBooks() throws
      Exception {
    Mockito
        .doNothing()
        .when(bookService)
        .deleteById(1L);
    mvc
        .perform(MockMvcRequestBuilders
            .post("/books/delete")
            .queryParam("id",
                "1")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isFound())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/books?message=Book+with+id+1+deleted%21"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("books"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("message",
                "Book with id 1 deleted!"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("entityClass"));
  }

  @Test
  void shouldBeReturnCode302CreateBookRedirectToBooks() throws
      Exception {
    Book childIslandBook = bookTestDataComponent.getChildIslandBook();
    Author exceptedAuthor = new Author();
    exceptedAuthor.setId(childIslandBook
        .getAuthor()
        .getId());
    childIslandBook.setAuthor(exceptedAuthor);
    Genre exceptedGenre = new Genre();
    exceptedGenre.setId(childIslandBook
        .getGenre()
        .getId());
    childIslandBook.setGenre(exceptedGenre);
    BookFullDto childIslandBookDto = bookMapper.toFullDto(childIslandBook);
    Mockito
        .doAnswer(invocation -> {
          BookFullDto bookFullDto = invocation.getArgument(0);
          bookFullDto.setId(3L);
          return bookFullDto;
        })
        .when(bookService)
        .saveOrUpdate(childIslandBookDto);
    mvc
        .perform(MockMvcRequestBuilders
            .post("/books")
            .queryParam("title",
                childIslandBook.getTitle())
            .queryParam("isbn",
                childIslandBook.getIsbn())
            .queryParam("author.id",
                String.valueOf(childIslandBook
                    .getAuthor()
                    .getId()))
            .queryParam("genre.id",
                String.valueOf(childIslandBook
                    .getGenre()
                    .getId()))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isFound())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/books?message=Book+with+id+3+saved%21"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("books"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("message",
                "Book with id 3 saved!"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("entityClass"));
  }

  @Test
  void shouldBeReturnCode302UpdateBookRedirectToBooks() throws
      Exception {
    Book girlFromEarthBook = bookTestDataComponent.getGirlFromEarthBook();
    girlFromEarthBook.setTitle("Девочка с Земли");
    Author exceptedAuthor = new Author();
    exceptedAuthor.setId(girlFromEarthBook
        .getAuthor()
        .getId());
    girlFromEarthBook.setAuthor(exceptedAuthor);
    Genre exceptedGenre = new Genre();
    exceptedGenre.setId(girlFromEarthBook
        .getGenre()
        .getId());
    girlFromEarthBook.setGenre(exceptedGenre);
    BookFullDto girlFromEarthBookDto = bookMapper.toFullDto(girlFromEarthBook);
    Mockito
        .doAnswer(invocation -> invocation.getArgument(0))
        .when(bookService)
        .saveOrUpdate(girlFromEarthBookDto);
    mvc
        .perform(MockMvcRequestBuilders
            .post("/books")
            .queryParam("id",
                String.valueOf(girlFromEarthBook.getId()))
            .queryParam("title",
                girlFromEarthBook.getTitle())
            .queryParam("isbn",
                girlFromEarthBook.getIsbn())
            .queryParam("author.id",
                String.valueOf(girlFromEarthBook
                    .getAuthor()
                    .getId()))
            .queryParam("genre.id",
                String.valueOf(girlFromEarthBook
                    .getGenre()
                    .getId()))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isFound())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/books?message=Book+with+id+1+saved%21"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("books"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("message",
                "Book with id 1 saved!"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("entityClass"));
  }

  @Test
  void shouldBeReturnCode400CreateOrUpdateBookThrowDuplicateKeyException() throws
      Exception {
    Book girlFromEarthBook = bookTestDataComponent.getGirlFromEarthBook();
    girlFromEarthBook.setId(null);
    Author exceptedAuthor = new Author();
    exceptedAuthor.setId(girlFromEarthBook
        .getAuthor()
        .getId());
    girlFromEarthBook.setAuthor(exceptedAuthor);
    Genre exceptedGenre = new Genre();
    exceptedGenre.setId(girlFromEarthBook
        .getGenre()
        .getId());
    girlFromEarthBook.setGenre(exceptedGenre);
    BookFullDto girlFromEarthBookDto = bookMapper.toFullDto(girlFromEarthBook);
    Mockito
        .doThrow(new LibraryGeneralException("Can't create author",
            new DuplicateKeyException("unique constraint violation")))
        .when(bookService)
        .saveOrUpdate(girlFromEarthBookDto);
    mvc
        .perform(MockMvcRequestBuilders
            .post("/books")
            .queryParam("title",
                girlFromEarthBook.getTitle())
            .queryParam("isbn",
                girlFromEarthBook.getIsbn())
            .queryParam("author.id",
                String.valueOf(girlFromEarthBook
                    .getAuthor()
                    .getId()))
            .queryParam("genre.id",
                String.valueOf(girlFromEarthBook
                    .getGenre()
                    .getId()))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isBadRequest())
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("books",
                "message",
                "entityClass"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("error"));
  }

  @Test
  void shouldBeReturnCode400CreateOrUpdateBookThrowBindException() throws
      Exception {
    Book girlFromEarthBook = bookTestDataComponent.getGirlFromEarthBook();
    girlFromEarthBook.setId(null);
    girlFromEarthBook.setTitle(girlFromEarthBook.getTitle() + "()");
    Author exceptedAuthor = new Author();
    exceptedAuthor.setId(girlFromEarthBook
        .getAuthor()
        .getId());
    girlFromEarthBook.setAuthor(exceptedAuthor);
    Genre exceptedGenre = new Genre();
    exceptedGenre.setId(girlFromEarthBook
        .getGenre()
        .getId());
    girlFromEarthBook.setGenre(exceptedGenre);
    mvc
        .perform(MockMvcRequestBuilders
            .post("/books")
            .queryParam("title",
                girlFromEarthBook.getTitle())
            .queryParam("isbn",
                girlFromEarthBook.getIsbn())
            .queryParam("author.id",
                String.valueOf(girlFromEarthBook
                    .getAuthor()
                    .getId()))
            .queryParam("genre.id",
                String.valueOf(girlFromEarthBook
                    .getGenre()
                    .getId()))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isBadRequest())
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("books",
                "message"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("error",
                "entityClass"));
  }

}