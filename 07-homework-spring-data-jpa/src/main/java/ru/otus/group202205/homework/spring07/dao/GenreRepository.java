package ru.otus.group202205.homework.spring07.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.group202205.homework.spring07.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
