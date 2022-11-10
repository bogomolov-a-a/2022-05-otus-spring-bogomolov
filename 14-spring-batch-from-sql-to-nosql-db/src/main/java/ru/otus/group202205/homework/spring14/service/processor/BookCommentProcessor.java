package ru.otus.group202205.homework.spring14.service.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring14.model.jpa.BookCommentEntity;
import ru.otus.group202205.homework.spring14.model.mongo.BookCommentDocument;
import ru.otus.group202205.homework.spring14.model.mongo.BookDocument;

@Service
public class BookCommentProcessor implements ItemProcessor<BookCommentEntity, BookCommentDocument> {

  @Override
  public BookCommentDocument process(BookCommentEntity item) {
    BookCommentDocument result = new BookCommentDocument();
    result.setId(item
        .getId()
        .toString());
    result.setText(item.getText());
    result.setCreated(item.getCreated());
    BookDocument resultBook = new BookDocument();
    resultBook.setId(item
        .getBook()
        .getId()
        .toString());
    result.setBook(resultBook);
    return result;
  }

}
