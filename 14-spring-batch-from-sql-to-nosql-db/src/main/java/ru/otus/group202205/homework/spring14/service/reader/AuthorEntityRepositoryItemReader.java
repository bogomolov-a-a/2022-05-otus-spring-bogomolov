package ru.otus.group202205.homework.spring14.service.reader;

import java.util.Map;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring14.dao.jpa.AuthorEntityRepository;
import ru.otus.group202205.homework.spring14.model.jpa.AuthorEntity;

@Service
public class AuthorEntityRepositoryItemReader extends RepositoryItemReader<AuthorEntity> {

  public AuthorEntityRepositoryItemReader(AuthorEntityRepository authorEntityRepository) {
    setRepository(authorEntityRepository);
    setMethodName("findAll");
    setSort(Map.of("id",
        Direction.ASC));
    setPageSize(10);
  }

}
