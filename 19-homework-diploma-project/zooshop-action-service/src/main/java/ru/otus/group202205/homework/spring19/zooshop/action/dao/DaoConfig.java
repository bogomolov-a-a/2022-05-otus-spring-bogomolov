package ru.otus.group202205.homework.spring19.zooshop.action.dao;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@EntityScan(basePackages = "ru.otus.group202205.homework.spring19.zooshop.action.model")
@Configuration
public class DaoConfig {

}
