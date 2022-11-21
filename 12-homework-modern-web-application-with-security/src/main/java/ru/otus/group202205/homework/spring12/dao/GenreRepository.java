package ru.otus.group202205.homework.spring12.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.group202205.homework.spring12.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
