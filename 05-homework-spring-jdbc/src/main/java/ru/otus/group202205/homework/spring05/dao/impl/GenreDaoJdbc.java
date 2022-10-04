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
import ru.otus.group202205.homework.spring05.dao.GenreDao;
import ru.otus.group202205.homework.spring05.model.Genre;

@Component
@RequiredArgsConstructor
public class GenreDaoJdbc implements GenreDao {

  private final NamedParameterJdbcOperations namedParameterJdbcOperations;

  @Override
  public List<Genre> getAll() {
    return namedParameterJdbcOperations.query("select g.id as genreId, g.name as genreName from genres g",
        new GenreRowMapper());
  }

  @Override
  public Genre getById(Long id) {
    return namedParameterJdbcOperations.queryForObject("select g.id as genreId, g.name as genreName from genres g where g.id=:id",
        new MapSqlParameterSource(Map.of("id",
            id)),
        new GenreRowMapper());
  }

  @Override
  public void insert(Genre genre) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    SqlParameterSource genreMapSqlParameterSource = createMapSqlParameterSource(genre);
    namedParameterJdbcOperations.update("insert into genres(name) values(:name)",
        genreMapSqlParameterSource,
        keyHolder);
    genre.setId((Long) keyHolder.getKey());
  }

  @Override
  public void update(Genre genre) {
    SqlParameterSource genreMapSqlParameterSource = createMapSqlParameterSource(genre).addValue("id",
        genre.getId());
    namedParameterJdbcOperations.update("update genres set name=:name where id=:id",
        genreMapSqlParameterSource);
  }

  @Override
  public void deleteById(Long id) {
    namedParameterJdbcOperations.update("delete from genres  where id=:id",
        new MapSqlParameterSource(Map.of("id",
            id)));
  }

  private MapSqlParameterSource createMapSqlParameterSource(Genre genre) {
    return new MapSqlParameterSource(Map.of("name",
        genre.getName()));
  }

  private static class GenreRowMapper implements RowMapper<Genre> {

    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws
        SQLException {
      Genre result = new Genre();
      result.setId(rs.getLong("genreId"));
      result.setName(rs.getString("genreName"));
      return result;
    }

  }

}
