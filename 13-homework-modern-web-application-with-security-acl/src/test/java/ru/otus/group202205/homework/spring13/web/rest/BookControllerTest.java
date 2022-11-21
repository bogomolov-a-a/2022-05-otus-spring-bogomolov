package ru.otus.group202205.homework.spring13.web.rest;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.group202205.homework.spring13.dao.UserRepository;
import ru.otus.group202205.homework.spring13.dto.AuthorDto;
import ru.otus.group202205.homework.spring13.dto.BookFullDto;
import ru.otus.group202205.homework.spring13.dto.GenreDto;
import ru.otus.group202205.homework.spring13.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring13.model.Author;
import ru.otus.group202205.homework.spring13.model.Book;
import ru.otus.group202205.homework.spring13.model.Genre;
import ru.otus.group202205.homework.spring13.service.BookService;
import ru.otus.group202205.homework.spring13.service.mapper.BookMapper;
import ru.otus.group202205.homework.spring13.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring13.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring13.testdata.GenreTestDataComponent;
import ru.otus.group202205.homework.spring13.web.security.WebSecurityConfig;

@WebMvcTest(BookController.class)
@Import({BookTestDataComponent.class, AuthorTestDataComponent.class, GenreTestDataComponent.class, WebSecurityConfig.class})
class BookControllerTest {

  private static final ObjectWriter BOOK_DTO_OBJECT_WRITER;
  private static final ObjectWriter BOOK_DTO_LIST_OBJECT_WRITER;

  static {
    ObjectMapper mapper = new ObjectMapper();
    BOOK_DTO_OBJECT_WRITER = mapper
        .writer()
        .forType(BookFullDto.class);
    BOOK_DTO_LIST_OBJECT_WRITER = mapper
        .writer()
        .forType(TypeFactory
            .defaultInstance()
            .constructCollectionType(List.class,
                BookFullDto.class));
  }

  @Autowired
  private MockMvc mvc;
  @Autowired
  private BookTestDataComponent bookTestDataComponent;
  @MockBean
  private BookService bookService;
  @MockBean
  private BookMapper bookMapper;
  @MockBean
  private UserRepository userRepository;

  @BeforeEach
  void init() {
    Mockito.reset(bookService);
    Mockito.reset(bookMapper);
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
  }

  @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
  @Test
  void shouldBeReturnCode200BookReadCardPageWithId() throws
      Exception {
    BookFullDto bookFullDto = bookTestDataComponent.getGirlFromEarthBookDto();
    Mockito
        .doReturn(bookFullDto)
        .when(bookService)
        .findById(1L);
    mvc
        .perform(MockMvcRequestBuilders
            .get("/books/1"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isOk())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(BOOK_DTO_OBJECT_WRITER.writeValueAsString(bookFullDto)));
  }

  @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
  @Test
  void shouldBeReturnCode200ReadBooks() throws
      Exception {
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
            .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(BOOK_DTO_LIST_OBJECT_WRITER.writeValueAsString(bookDtos)));
  }

  @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
  @Test
  void shouldBeReturnCode200DeleteBookRedirectToBooks() throws
      Exception {
    Mockito
        .doNothing()
        .when(bookService)
        .deleteById(1L);
    mvc
        .perform(MockMvcRequestBuilders
            .delete("/books/1"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isOk())
    ;
  }

  @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
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
        .andExpect(MockMvcResultMatchers.redirectedUrl("/books/page"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("books",
                "error",
                "message",
                "entityClass"));
  }

  @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
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
        .andExpect(MockMvcResultMatchers.redirectedUrl("/books/page"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("books",
                "error",
                "message",
                "entityClass"));
  }

  @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
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

  @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
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