package ru.otus.group202205.homework.spring10.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.group202205.homework.spring10.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
