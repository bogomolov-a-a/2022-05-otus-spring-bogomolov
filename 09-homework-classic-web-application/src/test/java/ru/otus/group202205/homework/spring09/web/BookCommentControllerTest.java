package ru.otus.group202205.homework.spring09.web;

import static org.mockito.ArgumentMatchers.any;

import java.io.File;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
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
import ru.otus.group202205.homework.spring09.dto.BookCommentDto;
import ru.otus.group202205.homework.spring09.dto.BookFullDto;
import ru.otus.group202205.homework.spring09.dto.BookSimpleDto;
import ru.otus.group202205.homework.spring09.dto.GenreDto;
import ru.otus.group202205.homework.spring09.exception.LibraryGeneralException;
import ru.otus.group202205.homework.spring09.model.Author;
import ru.otus.group202205.homework.spring09.model.Book;
import ru.otus.group202205.homework.spring09.model.BookComment;
import ru.otus.group202205.homework.spring09.model.Genre;
import ru.otus.group202205.homework.spring09.service.BookCommentService;
import ru.otus.group202205.homework.spring09.service.BookService;
import ru.otus.group202205.homework.spring09.service.mapper.BookCommentMapper;
import ru.otus.group202205.homework.spring09.service.mapper.BookMapper;
import ru.otus.group202205.homework.spring09.testdata.AuthorTestDataComponent;
import ru.otus.group202205.homework.spring09.testdata.BookCommentTestDataComponent;
import ru.otus.group202205.homework.spring09.testdata.BookTestDataComponent;
import ru.otus.group202205.homework.spring09.testdata.GenreTestDataComponent;
import ru.otus.group202205.homework.spring09.web.webutil.ActionMode;
import ru.otus.group202205.homework.spring09.web.webutil.CardService;

@WebMvcTest(BookCommentController.class)
@Import({BookCommentTestDataComponent.class, BookTestDataComponent.class, AuthorTestDataComponent.class, GenreTestDataComponent.class})
class BookCommentControllerTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private BookCommentTestDataComponent bookCommentTestDataComponent;
  @Autowired
  private BookTestDataComponent bookTestDataComponent;
  @MockBean
  private BookCommentService bookCommentService;
  @MockBean
  private CardService cardService;
  @MockBean
  private BookService bookService;
  @MockBean
  private BookMapper bookMapper;
  @MockBean
  private BookCommentMapper bookCommentMapper;

  @BeforeEach
  void init() {
    Mockito.reset(cardService);
    Mockito.reset(bookService);
    Mockito.reset(bookMapper);
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
  void shouldBeReturnBookCreateCardPage() throws
      Exception {
    File exceptedFile = new ClassPathResource("/controller-test/book-comments/get-book-comments/card-to-create.html",
        BookCommentControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders
            .get("/book-comment")
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
    File exceptedFile = new ClassPathResource("/controller-test/book-comments/get-book-comments/read-update-card-id-error.html",
        BookCommentControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders
            .get("/book-comment")
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
    File exceptedFile = new ClassPathResource("/controller-test/book-comments/get-book-comments/read-update-card-id-error.html",
        BookCommentControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders
            .get("/book-comment")
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
                "Update bookcomment page"))
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
    File exceptedFile = new ClassPathResource("/controller-test/book-comments/get-book-comments/delete-card-not-allowed-error.html",
        BookCommentControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    mvc
        .perform(MockMvcRequestBuilders
            .get("/book-comment")
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
            .attributeDoesNotExist("bookComment"))
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
    File exceptedFile = new ClassPathResource("/controller-test/book-comments/get-book-comments/read-book-comment-200-id-1-read.html",
        BookCommentControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
    BookCommentDto bookCommentDto = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBookSimpleDto();
    Mockito
        .doReturn(bookCommentDto)
        .when(bookCommentService)
        .findById(1L);
    mvc
        .perform(MockMvcRequestBuilders
            .get("/book-comment")
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
                "Read bookcomment page"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("bookComment",
                bookCommentDto))
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
    File exceptedFile = new ClassPathResource("/controller-test/book-comments/get-book-comments/read-book-comments-200.html",
        BookCommentControllerTest.class.getClassLoader()).getFile();
    String exceptedContent = Files.readString(exceptedFile.toPath());
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
            .contentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("bookComments",
                bookCommentDtos))
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
            .post("/book-comments/delete")
            .queryParam("id",
                "1")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers
            .status()
            .isFound())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/book-comments?message=Book+comment+with+id+1+deleted%21"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("bookComments"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("message",
                "Book comment with id 1 deleted!"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("entityClass"));
  }

  @Test
  void shouldBeReturnCode302CreateBookRedirectToBooks() throws
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
        .andExpect(MockMvcResultMatchers.redirectedUrl("/book-comments?message=Book+comment+with+id+3+saved%21"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("bookComments"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("message",
                "Book comment with id 3 saved!"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("entityClass"));
  }

  @Test
  void shouldBeReturnCode302UpdateBookRedirectToBooks() throws
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
        .andExpect(MockMvcResultMatchers.redirectedUrl("/book-comments?message=Book+comment+with+id+1+saved%21"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("bookComments"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("error"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attribute("message",
                "Book comment with id 1 saved!"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeDoesNotExist("entityClass"));
  }

  @Test
  void shouldBeReturnCode400CreateOrUpdateBookThrowDuplicateKeyException() throws
      Exception {
    BookComment niceBookCommentForGirlFromTheEarthBook = bookCommentTestDataComponent.getNiceBookCommentForGirlFromTheEarthBook();
    niceBookCommentForGirlFromTheEarthBook.setId(null);
    BookCommentDto niceBookCommentForGirlFromTheEarthBookDto = bookCommentMapper.toDto(niceBookCommentForGirlFromTheEarthBook);
    Mockito
        .doThrow(new LibraryGeneralException("Can't create author",
            new DuplicateKeyException("unique constraint violation")))
        .when(bookCommentService)
        .saveOrUpdate(niceBookCommentForGirlFromTheEarthBookDto);
    mvc
        .perform(MockMvcRequestBuilders
            .post("/book-comments")
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
            .post("/book-comments")
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
            .attributeDoesNotExist("bookComments",
                "message"))
        .andExpect(MockMvcResultMatchers
            .model()
            .attributeExists("error",
                "entityClass"));
  }

}