package ru.otus.group202205.homework.spring10.web.rest;

import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
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
import ru.otus.group202205.homework.spring10.dto.AuthorDto;
import ru.otus.group202205.homework.spring10.dto.BookCommentDto;
import ru.otus.group202205.homework.spring10.dto.BookFullDto;
import ru.otus.group202205.homework.spring10.dto.BookSimpleDto;
import ru.otus.group202205.homework.spring10.dto.GenreDto;
import ru.otus.group202205.homework.spring10.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring10.model.Author;
import ru.otus.group202205.homework.spring10.model.Book;
import ru.otus.group202205.homework.spring10.model.BookComment;
import ru.otus.group202205.homework.spring10.model.Genre;
import ru.otus.group202205.homework.spring10.service.BookCommentService;
import ru.otus.group202205.homework.spring10.service.BookService;
import ru.otus.group202205.homework.spring10.service.mapper.BookCommentMapper;
import ru.otus.group202205.homework.spring10.service.mapper.BookMapper;
import ru.otus.group202205.homework.spring10.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring10.testdata.BookCommentTestDataComponent;
import ru.otus.group202205.homework.spring10.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring10.testdata.GenreTestDataComponent;

@WebMvcTest(BookCommentController.class)
@Import({BookCommentTestDataComponent.class, BookTestDataComponent.class, AuthorTestDataComponent.class, GenreTestDataComponent.class})
class BookCommentControllerTest {

  private static final ObjectWriter BOOK_COMMENT_DTO_OBJECT_WRITER;
  private static final ObjectWriter BOOK_COMMENT_DTO_LIST_OBJECT_WRITER;

  static {
    ObjectMapper mapper = new ObjectMapper();
    DateFormat dateFormat = StdDateFormat.getInstance();
    BOOK_COMMENT_DTO_OBJECT_WRITER = mapper
        .writer()
        .forType(BookCommentDto.class)
        .with(dateFormat);
    BOOK_COMMENT_DTO_LIST_OBJECT_WRITER = mapper
        .writer()
        .forType(TypeFactory
            .defaultInstance()
            .constructCollectionType(List.class,
                BookCommentDto.class))
        .with(dateFormat);
  }

  @Autowired
  private MockMvc mvc;
  @Autowired
  private BookCommentTestDataComponent bookCommentTestDataComponent;
  @MockBean
  private BookCommentService bookCommentService;
  @MockBean
  private BookService bookService;
  @MockBean
  private BookMapper bookMapper;
  @MockBean
  private BookCommentMapper bookCommentMapper;

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
    Mockito.reset(bookCommentMapper);
    Mockito
        .doAnswer(invocation -> {
          BookComment bookComment = invocation.getArgument(0);
          BookCommentDto result = new BookCommentDto();
          result.setId(bookComment.getId());
          result.setText(bookComment.getText());
          result.setCreated(bookComment.getCreated());
          Book book = bookComment.getBook();
          if (book != null) {
            BookSimpleDto bookDto = new BookSimpleDto();
            bookDto.setId(book.getId());
            result.setBook(bookDto);
          }
          return result;
        })
        .when(bookCommentMapper)
        .toDto(any());
    Mockito
        .doAnswer(invocation -> {
          BookCommentDto bookCommentDto = invocation.getArgument(0);
          BookComment result = new BookComment();
          result.setId(bookCommentDto.getId());
          result.setText(bookCommentDto.getText());
          result.setCreated(bookCommentDto.getCreated());
          BookSimpleDto bookDto = bookCommentDto.getBook();
          if (bookDto != null) {
            Book book = new Book();
            book.setId(bookDto.getId());
            result.setBook(book);
          }
          return result;
        })
        .when(bookCommentMapper)
        .toEntity(any());
  }


  @Test
  void shouldBeReturnCode200ReadBookComments() throws
      Exception {
    List<BookCommentDto> bookCommentDtos = bookCommentTestDataComponent
        .getAllBookComments()
        .stream()
        .map(bookCommentMapper::toDto)
        .collect(Collectors.toList());
    Mockito
        .doReturn(bookCommentDtos)
        .when(bookCommentService)
        .findAll();
    mvc
        .perform(MockMvcRequestBuilders.get("/book-comments"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isOk())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(BOOK_COMMENT_DTO_LIST_OBJECT_WRITER.writeValueAsString(bookCommentDtos)));
  }

  @Test
  void shouldBeReturnCode200ReadBookComment() throws
      Exception {
    BookCommentDto bookCommentDto = bookCommentTestDataComponent
        .getNiceBookCommentForGirlFromTheEarthBookSimpleDto();
    Mockito
        .doReturn(bookCommentDto)
        .when(bookCommentService)
        .findById(1L);
    mvc
        .perform(MockMvcRequestBuilders.get("/book-comments/1"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isOk())
        .andExpect(MockMvcResultMatchers
            .content()
            .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .content()
            .string(BOOK_COMMENT_DTO_OBJECT_WRITER.writeValueAsString(bookCommentDto)));
  }

  @Test
  void shouldBeReturnCode302DeleteBookCommentRedirectToBookComments() throws
      Exception {
    Mockito
        .doNothing()
        .when(bookService)
        .deleteById(1L);
    mvc
        .perform(MockMvcRequestBuilders
            .delete("/book-comments/1"))
        .andExpect(MockMvcResultMatchers
            .status()
            .isFound())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/book-comments/page"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("bookComments",
                "error",
                "message",
                "entityClass"));
  }

  @Test
  void shouldBeReturnCode302CreateBookCommentRedirectToBookComments() throws
      Exception {
    BookComment aliceMielophonJokeComment = bookCommentTestDataComponent.getAliceMielophonJokeCommentForGirlFromTheEarthBook();

    BookCommentDto aliceMielophonJokeCommentDto = bookCommentMapper.toDto(aliceMielophonJokeComment);
    Mockito
        .doAnswer(invocation -> {
          BookCommentDto bookFullDto = invocation.getArgument(0);
          bookFullDto.setId(3L);
          return bookFullDto;
        })
        .when(bookCommentService)
        .saveOrUpdate(aliceMielophonJokeCommentDto);
    mvc
        .perform(MockMvcRequestBuilders
            .post("/book-comments")
            .queryParam("text",
                aliceMielophonJokeComment.getText())
            .queryParam("created",
                aliceMielophonJokeComment
                    .getCreated()
                    .format(DateTimeFormatter.ISO_DATE_TIME))
            .queryParam("book.id",
                String.valueOf(aliceMielophonJokeComment
                    .getBook()
                    .getId()))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isFound())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/book-comments/page"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("bookComments",
                "error",
                "message",
                "entityClass"));
  }

  @Test
  void shouldBeReturnCode302UpdateBookCommentRedirectToBookComments() throws
      Exception {
    BookComment niceBookCommentForGirlFromTheEarthBook = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBook();
    niceBookCommentForGirlFromTheEarthBook.setText("Отличная книга!");
    BookCommentDto niceBookCommentForGirlFromTheEarthBookDto = bookCommentMapper.toDto(niceBookCommentForGirlFromTheEarthBook);
    Mockito
        .doAnswer(invocation -> invocation.getArgument(0))
        .when(bookCommentService)
        .saveOrUpdate(niceBookCommentForGirlFromTheEarthBookDto);
    mvc
        .perform(MockMvcRequestBuilders
            .post("/book-comments")
            .queryParam("id",
                String.valueOf(niceBookCommentForGirlFromTheEarthBook.getId()))
            .queryParam("text",
                niceBookCommentForGirlFromTheEarthBook.getText())
            .queryParam("created",
                niceBookCommentForGirlFromTheEarthBook
                    .getCreated()
                    .format(DateTimeFormatter.ISO_DATE_TIME))
            .queryParam("book.id",
                String.valueOf(niceBookCommentForGirlFromTheEarthBook
                    .getBook()
                    .getId()))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isFound())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/book-comments/page"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("bookComments",
                "error",
                "message",
                "entityClass"));
  }

  @Test
  void shouldBeReturnCode400CreateOrUpdateBookThrowDuplicateKeyException() throws
      Exception {
    BookComment niceBookCommentForGirlFromTheEarthBookComment = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBook();
    niceBookCommentForGirlFromTheEarthBookComment.setId(null);
    BookCommentDto niceBookCommentForGirlFromTheEarthBookDto = bookCommentMapper.toDto(niceBookCommentForGirlFromTheEarthBookComment);
    Mockito
        .doThrow(new LibraryGeneralException("Can't create author",
            new DuplicateKeyException("unique constraint violation")))
        .when(bookCommentService)
        .saveOrUpdate(niceBookCommentForGirlFromTheEarthBookDto);
    mvc
        .perform(MockMvcRequestBuilders
            .post("/book-comments")
            .queryParam("text",
                niceBookCommentForGirlFromTheEarthBookComment.getText())
            .queryParam("created",
                niceBookCommentForGirlFromTheEarthBookComment
                    .getCreated()
                    .format(DateTimeFormatter.ISO_DATE_TIME))
            .queryParam("book.id",
                String.valueOf(niceBookCommentForGirlFromTheEarthBookComment
                    .getBook()
                    .getId()))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isBadRequest())
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("bookComments",
                "message",
                "entityClass"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("error"));
  }

}