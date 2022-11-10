package ru.otus.group202205.homework.spring14.service.writer;

import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring14.dao.mongo.BookDocumentRepository;
import ru.otus.group202205.homework.spring14.model.mongo.BookDocument;

@Service
public class BookDocumentItemWriter extends RepositoryItemWriter<BookDocument> {

  public BookDocumentItemWriter(BookDocumentRepository bookDocumentRepository) {
    setRepository(bookDocumentRepository);
  }

}
