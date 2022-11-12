package ru.otus.group202205.homework.spring08.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.group202205.homework.spring08.model.BookComment;

public interface BookCommentRepository extends MongoRepository<BookComment, String> {

}
