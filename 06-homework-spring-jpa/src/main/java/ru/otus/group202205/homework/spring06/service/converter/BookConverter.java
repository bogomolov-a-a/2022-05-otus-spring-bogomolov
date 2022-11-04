package ru.otus.group202205.homework.spring06.service.converter;

import java.util.List;
import ru.otus.group202205.homework.spring06.dto.BookFullDto;

public interface BookConverter {

  String convertBook(BookFullDto book);

  String convertBooks(List<BookFullDto> books);

}
