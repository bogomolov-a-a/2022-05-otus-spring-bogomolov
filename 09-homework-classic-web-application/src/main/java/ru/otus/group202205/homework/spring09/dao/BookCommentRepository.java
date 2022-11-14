package ru.otus.group202205.homework.spring09.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.group202205.homework.spring09.model.BookComment;

public interface BookCommentRepository extends JpaRepository<BookComment, Long> {

}
