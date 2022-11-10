package ru.otus.group202205.homework.spring14.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.group202205.homework.spring14.model.jpa.BookEntity;

public interface BookEntityRepository extends JpaRepository<BookEntity, Long> {


}
