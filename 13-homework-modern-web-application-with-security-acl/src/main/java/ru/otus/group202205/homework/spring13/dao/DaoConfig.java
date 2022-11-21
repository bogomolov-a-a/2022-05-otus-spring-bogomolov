package ru.otus.group202205.homework.spring13.dao;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "ru.otus.group202205.homework.spring13.model")
@EnableJpaRepositories
public class DaoConfig {

}
