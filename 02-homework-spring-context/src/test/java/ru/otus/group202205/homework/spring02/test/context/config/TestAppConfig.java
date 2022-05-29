package ru.otus.group202205.homework.spring02.test.context.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.group202205.homework.spring02.dao.DaoConfig;
import ru.otus.group202205.homework.spring02.service.ServiceConfig;
import ru.otus.group202205.homework.spring02.util.UtilConfig;

/**
 * Default context configuration for tests(no default app with properties sources).
 */
@Import({DaoConfig.class, ServiceConfig.class, UtilConfig.class})
@Configuration
public class TestAppConfig {

}
