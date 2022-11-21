package ru.otus.group202205.homework.spring13.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.group202205.homework.spring13.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {


}
