package ru.otus.group202205.homework.spring05.service;

import java.util.List;
import ru.otus.group202205.homework.spring05.model.Book;

public interface BookConverter {

  String convertBook(Book book);

  String convertBooks(List<Book> books);

}
