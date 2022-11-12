package ru.otus.group202205.homework.spring14.service.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring14.model.jpa.AuthorEntity;
import ru.otus.group202205.homework.spring14.model.mongo.AuthorDocument;

@Service
public class AuthorProcessor implements ItemProcessor<AuthorEntity, AuthorDocument> {

  @Override
  public AuthorDocument process(AuthorEntity item) {
    AuthorDocument result = new AuthorDocument();
    result.setId(item
        .getId()
        .toString());
    result.setName(item.getName());
    result.setSurname(item.getSurname());
    result.setPatronymic(item.getPatronymic());
    result.setBirthYear(item.getBirthYear());
    result.setDeathYear(item.getDeathYear());
    return result;
  }

}
