package ru.otus.group202205.homework.spring14.dao.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.group202205.homework.spring14.model.mongo.AuthorDocument;

public interface AuthorDocumentRepository extends MongoRepository<AuthorDocument, String> {

}
