package ru.otus.group202205.homework.spring14.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.group202205.homework.spring14.model.jpa.AuthorEntity;

public interface AuthorEntityRepository extends JpaRepository<AuthorEntity, Long> {

}
