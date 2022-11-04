package ru.otus.group202205.homework.spring06.service.converter.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring06.dto.AuthorDto;
import ru.otus.group202205.homework.spring06.service.converter.AuthorConverter;

@Service
public class AuthorConverterImpl implements AuthorConverter {

  @Override
  public String convertAuthor(AuthorDto author) {
    return String.format("Author id: %d%ssurname: %s%sname: %s%spatronymic: %s%sbirth year: %d%sdeath year: %s%s",
        author.getId(),
        System.lineSeparator(),
        author.getSurname(),
        System.lineSeparator(),
        author.getName(),
        System.lineSeparator(),
        author.getPatronymic(),
        System.lineSeparator(),
        author.getBirthYear(),
        System.lineSeparator(),
        author.getDeathYear(),
        System.lineSeparator());
  }

  @Override
  public String convertAuthors(List<AuthorDto> authors) {
    StringBuilder result = new StringBuilder("Author list").append(System.lineSeparator());
    authors.forEach(author -> result.append(convertAuthor(author)));
    return result.toString();
  }

}
