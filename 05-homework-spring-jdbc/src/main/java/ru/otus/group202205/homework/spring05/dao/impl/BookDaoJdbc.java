package ru.otus.group202205.homework.spring05.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring05.dao.BookDao;
import ru.otus.group202205.homework.spring05.model.Author;
import ru.otus.group202205.homework.spring05.model.Book;
import ru.otus.group202205.homework.spring05.model.Genre;

@Component
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {

  private final NamedParameterJdbcOperations namedParameterJdbcOperations;

  @Override
  public List<Book> getAll() {
    return namedParameterJdbcOperations.query(
        "select b.id as bookId, b.title as bookTitle,b.isbn as bookIsbn, a.id as authorId, a.surname as authorSurname, a.name as authorName, "
            + "a.patronymic as authorPatronymic,a.birthYear as authorBirthYear, a.deathYear as authorDeathYear, g.id as genreId, g.name as GenreName "
            + "from books b,authors a,genres g where b.genre_id=g.id and b.author_id=a.id",
        new BookRowMapper());
  }

  @Override
  public Book getById(Long id) {
    return namedParameterJdbcOperations.queryForObject(
        "select b.id as bookId, b.title as bookTitle,b.isbn as bookIsbn, a.id as authorId,  a.surname as authorSurname, a.name as authorName, "
            + "a.patronymic as authorPatronymic,a.birthYear as authorBirthYear, a.deathYear as authorDeathYear, g.id as genreId, g.name as GenreName "
            + "from books b,authors a,genres g where  b.id=:id and b.genre_id=g.id and b.author_id=a.id",
        new MapSqlParameterSource(Map.of("id",
            id)),
        new BookRowMapper());
  }

  @Override
  public void insert(Book book) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    MapSqlParameterSource bookParameterSource = createBookParameterSource(book);
    namedParameterJdbcOperations.update("insert into books(title,isbn,author_id,genre_id) values(:title,:isbn,:authorId,:genreId)",
        bookParameterSource,
        keyHolder);
    book.setId((Long) keyHolder.getKey());
  }

  @Override
  public void update(Book book) {
    MapSqlParameterSource bookParameterSource = createBookParameterSource(book).addValue("id",
        book.getId());
    namedParameterJdbcOperations.update("update books set title=:title, isbn=:isbn,author_id=:authorId,genre_id=:genreId where id=:id",
        bookParameterSource);
  }

  @Override
  public void deleteById(Long id) {
    namedParameterJdbcOperations.update("delete from books where id=:id",
        new MapSqlParameterSource(Map.of("id",
            id)));
  }

  private MapSqlParameterSource createBookParameterSource(Book book) {
    MapSqlParameterSource result = new MapSqlParameterSource(Map.of("title",
        book.getTitle(),
        "isbn",
        book.getIsbn()));
    addAuthorIdParam(book,
        result);
    addGenreIdParam(book,
        result);
    return result;
  }

  private void addGenreIdParam(Book book, MapSqlParameterSource bookParameterSource) {
    if (isBookGenreIdNotNull(book.getGenre())) {
      bookParameterSource.addValue("genreId",
          book
              .getGenre()
              .getId());
      return;
    }
    bookParameterSource.addValue("genreId",
        null,
        Types.NULL);
  }

  private void addAuthorIdParam(Book book, MapSqlParameterSource bookParameterSource) {
    if (isBookAuthorIdIsNotNull(book.getAuthor())) {
      bookParameterSource.addValue("authorId",
          book
              .getAuthor()
              .getId());
      return;
    }
    bookParameterSource.addValue("authorId",
        null,
        Types.NULL);
  }

  private boolean isBookGenreIdNotNull(Genre genre) {
    return genre != null && genre.getId() != null;
  }

  private boolean isBookAuthorIdIsNotNull(Author author) {
    return author != null && author.getId() != null;
  }

  private static class BookRowMapper implements RowMapper<Book> {

    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws
        SQLException {
      Book result = new Book();
      result.setId(rs.getLong("bookId"));
      result.setTitle(rs.getString("bookTitle"));
      result.setIsbn(rs.getString("bookIsbn"));
      Author resultAuthor = new Author();
      result.setAuthor(resultAuthor);
      resultAuthor.setId(rs.getLong("authorId"));
      resultAuthor.setSurname(rs.getString("authorSurname"));
      resultAuthor.setName(rs.getString("authorName"));
      resultAuthor.setPatronymic(rs.getString("authorPatronymic"));
      resultAuthor.setBirthYear(rs.getLong("authorBirthYear"));
      long deathYear = rs.getLong("authorDeathYear");
      if (!rs.wasNull()) {
        resultAuthor.setDeathYear(deathYear);
      }
      Genre resultGenre = new Genre();
      result.setGenre(resultGenre);
      resultGenre.setId(rs.getLong("genreId"));
      resultGenre.setName(rs.getString("genreName"));
      return result;
    }

  }

}
