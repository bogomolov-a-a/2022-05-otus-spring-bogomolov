package ru.otus.group202205.homework.spring14.service.writer;

import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring14.dao.mongo.BookCommentDocumentRepository;
import ru.otus.group202205.homework.spring14.model.mongo.BookCommentDocument;

@Service
public class BookCommentDocumentItemWriter extends RepositoryItemWriter<BookCommentDocument> {

  public BookCommentDocumentItemWriter(BookCommentDocumentRepository bookCommentDocumentRepository) {
    setRepository(bookCommentDocumentRepository);
  }

}
