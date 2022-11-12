package ru.otus.group202205.homework.spring08.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.group202205.homework.spring08.model.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {

}
