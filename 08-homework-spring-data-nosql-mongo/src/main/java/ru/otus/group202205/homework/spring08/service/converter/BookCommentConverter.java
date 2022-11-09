package ru.otus.group202205.homework.spring08.service.converter;

import java.util.List;
import ru.otus.group202205.homework.spring08.dto.BookCommentDto;

public interface BookCommentConverter {

  String convertBookComment(BookCommentDto bookComment);

  String convertBookComments(List<BookCommentDto> bookComments);

}
