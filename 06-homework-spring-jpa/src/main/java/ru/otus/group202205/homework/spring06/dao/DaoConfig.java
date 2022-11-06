package ru.otus.group202205.homework.spring06.dao;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = "ru.otus.group202205.homework.spring06.model")
public class DaoConfig {

}
