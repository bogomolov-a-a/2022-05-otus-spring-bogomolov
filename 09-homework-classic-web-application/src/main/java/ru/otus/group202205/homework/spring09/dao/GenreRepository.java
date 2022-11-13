package ru.otus.group202205.homework.spring09.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.group202205.homework.spring09.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
