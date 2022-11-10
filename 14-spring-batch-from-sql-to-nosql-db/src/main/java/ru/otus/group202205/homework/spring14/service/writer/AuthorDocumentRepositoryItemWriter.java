package ru.otus.group202205.homework.spring14.service.writer;

import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring14.dao.mongo.AuthorDocumentRepository;
import ru.otus.group202205.homework.spring14.model.mongo.AuthorDocument;

@Service
public class AuthorDocumentRepositoryItemWriter extends RepositoryItemWriter<AuthorDocument> {

  public AuthorDocumentRepositoryItemWriter(AuthorDocumentRepository authorDocumentRepository) {
    setRepository(authorDocumentRepository);
  }

}
