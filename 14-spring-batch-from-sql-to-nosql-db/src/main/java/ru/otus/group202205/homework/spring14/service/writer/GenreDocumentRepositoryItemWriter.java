package ru.otus.group202205.homework.spring14.service.writer;

import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring14.dao.mongo.GenreDocumentRepository;
import ru.otus.group202205.homework.spring14.model.mongo.GenreDocument;

@Service
public class GenreDocumentRepositoryItemWriter extends RepositoryItemWriter<GenreDocument> {

  public GenreDocumentRepositoryItemWriter(GenreDocumentRepository genreDocumentRepository) {
    setRepository(genreDocumentRepository);
  }

}
