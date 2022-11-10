package ru.otus.group202205.homework.spring14.service.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring14.model.jpa.GenreEntity;
import ru.otus.group202205.homework.spring14.model.mongo.GenreDocument;

@Service
public class GenreProcessor implements ItemProcessor<GenreEntity, GenreDocument> {

  @Override
  public GenreDocument process(GenreEntity item) {
    GenreDocument result = new GenreDocument();
    result.setId(item
        .getId()
        .toString());
    result.setName(item.getName());
    return result;
  }

}
