package ru.otus.group202205.homework.spring06.service.converter.impl;

import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring06.dto.BookCommentDto;
import ru.otus.group202205.homework.spring06.service.converter.BookCommentConverter;

@Service
public class BookCommentConverterImpl implements BookCommentConverter {

  @Override
  public String convertBookComment(BookCommentDto bookComment) {
    return String.format("Comment id %d book with id %d at %s.%s with text: '%s'",
        bookComment.getId(),
        bookComment
            .getBook()
            .getId(),
        bookComment
            .getCreated()
            .format(DateTimeFormatter.ISO_DATE_TIME),
        System.lineSeparator(),
        bookComment.getText());
  }

  @Override
  public String convertBookComments(List<BookCommentDto> bookComments) {
    StringBuilder result = new StringBuilder("Comment list:").append(System.lineSeparator());
    bookComments.forEach(comment -> result
        .append(convertBookComment(comment))
        .append(System.lineSeparator()));
    return result.toString();
  }

}
