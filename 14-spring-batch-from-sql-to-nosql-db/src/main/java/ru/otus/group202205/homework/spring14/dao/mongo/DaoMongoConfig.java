package ru.otus.group202205.homework.spring14.dao.mongo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "ru.otus.group202205.homework.spring14.dao.mongo")
@RequiredArgsConstructor
@Slf4j
public class DaoMongoConfig {

}
