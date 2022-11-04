package ru.otus.group202205.homework.spring06.service.converter;

import java.util.List;
import ru.otus.group202205.homework.spring06.dto.AuthorDto;

public interface AuthorConverter {

  String convertAuthor(AuthorDto author);

  String convertAuthors(List<AuthorDto> authors);

}
