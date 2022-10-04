package ru.otus.group202205.homework.spring05.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring05.dao.AuthorDao;
import ru.otus.group202205.homework.spring05.model.Author;

@Component
@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {

  private final NamedParameterJdbcOperations namedParameterJdbcOperations;

  @Override
  public List<Author> getAll() {

    return namedParameterJdbcOperations.query(
        "select a.id as authorId, a.surname as authorSurname,a.name as authorName, a.patronymic as authorPatronymic, a.birthYear as authorBirthYear, "
            + "a.deathYear as authorDeathYear from authors a ",
        new AuthorRowMapper());
  }

  @Override
  public Author getById(Long id) {
    return namedParameterJdbcOperations.queryForObject(
        "select a.id as authorId, a.surname as authorSurname,a.name as authorName, a.patronymic as authorPatronymic, a.birthYear as authorBirthYear, "
            + "a.deathYear as authorDeathYear from authors a  where id=:id",
        Map.of("id",
            id),
        new AuthorRowMapper());
  }

  @Override
  public void insert(Author author) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    SqlParameterSource authorParameterSource = createAuthorMapSqlParameterSource(author);
    namedParameterJdbcOperations.update("insert into authors(surname,name,patronymic,birthYear,deathYear) values(:surname,:name,:patronymic,:birthYear,:deathYear)",
        authorParameterSource,
        keyHolder);
    author.setId((Long) keyHolder.getKey());

  }

  @Override
  public void update(Author author) {
    MapSqlParameterSource authorParameterSource = createAuthorMapSqlParameterSource(author)
        .addValue("id",
            author.getId());
    namedParameterJdbcOperations.update("update authors set surname=:surname,name=:name,patronymic=:patronymic,birthYear=:birthYear,deathYear=:deathYear where id=:id",
        authorParameterSource);
  }

  @Override
  public void deleteById(Long id) {
    MapSqlParameterSource authorParameterSource = new MapSqlParameterSource(Map.of("id",
        id));
    namedParameterJdbcOperations.update("delete from authors where id=:id",
        authorParameterSource);
  }

  private MapSqlParameterSource createAuthorMapSqlParameterSource(Author author) {
    MapSqlParameterSource result = new MapSqlParameterSource()
        .addValue("surname",
            author.getSurname())
        .addValue("name",
            author.getName())
        .addValue("patronymic",
            author.getPatronymic())
        .addValue("birthYear",
            author.getBirthYear());
    addDeathYear(author,
        result);
    return result;
  }

  private void addDeathYear(Author author, MapSqlParameterSource authorParameterSource) {
    if (author.getDeathYear() != null) {
      authorParameterSource.addValue("deathYear",
          author.getDeathYear());
      return;
    }
    authorParameterSource.addValue("deathYear",
        null,
        java.sql.Types.NULL);
  }

  private static class AuthorRowMapper implements RowMapper<Author> {

    @Override
    public Author mapRow(ResultSet rs, int rowNum) throws
        SQLException {
      Author result = new Author();
      result.setId(rs.getLong("authorId"));
      result.setSurname(rs.getString("authorSurname"));
      result.setName(rs.getString("authorName"));
      result.setPatronymic(rs.getString("authorPatronymic"));
      result.setBirthYear(rs.getLong("authorBirthYear"));
      long deathYear = rs.getLong("authorDeathYear");
      if (!rs.wasNull()) {
        result.setDeathYear(deathYear);
      }
      return result;
    }

  }

}
