package ru.otus.group202205.homework.spring05.service;

import java.util.List;
import ru.otus.group202205.homework.spring05.model.Author;

public interface AuthorConverter {

  String convertAuthor(Author author);

  String convertAuthors(List<Author> authors);

}
