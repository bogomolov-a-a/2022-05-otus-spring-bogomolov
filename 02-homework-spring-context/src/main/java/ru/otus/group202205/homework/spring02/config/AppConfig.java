package ru.otus.group202205.homework.spring02.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import ru.otus.group202205.homework.spring02.dao.DaoConfig;
import ru.otus.group202205.homework.spring02.service.ServiceConfig;
import ru.otus.group202205.homework.spring02.util.UtilConfig;

@Import({DaoConfig.class, ServiceConfig.class, UtilConfig.class})
@Configuration
@PropertySource("classpath:/application.properties")
public class AppConfig {

}
