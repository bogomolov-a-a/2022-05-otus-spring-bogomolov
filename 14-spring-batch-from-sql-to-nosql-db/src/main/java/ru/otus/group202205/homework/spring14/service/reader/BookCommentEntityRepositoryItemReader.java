package ru.otus.group202205.homework.spring14.service.reader;

import java.util.Map;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring14.dao.jpa.BookCommentEntityRepository;
import ru.otus.group202205.homework.spring14.model.jpa.BookCommentEntity;

@Service
public class BookCommentEntityRepositoryItemReader extends RepositoryItemReader<BookCommentEntity> {

  public BookCommentEntityRepositoryItemReader(BookCommentEntityRepository bookCommentEntityRepository) {
    setRepository(bookCommentEntityRepository);
    setMethodName("findAll");
    setSort(Map.of("id",
        Direction.ASC));
    setPageSize(10);
  }

}
