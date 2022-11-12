package ru.otus.group202205.homework.spring08.service.converter;

import java.util.List;
import ru.otus.group202205.homework.spring08.dto.AuthorDto;

public interface AuthorConverter {

  String convertAuthor(AuthorDto author);

  String convertAuthors(List<AuthorDto> authors);

}
