package ru.otus.group202205.homework.spring14.dao.jpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "ru.otus.group202205.homework.spring14.model.jpa")
@EnableJpaRepositories(basePackages = "ru.otus.group202205.homework.spring14.dao.jpa")
public class DaoJpaConfig {

}
