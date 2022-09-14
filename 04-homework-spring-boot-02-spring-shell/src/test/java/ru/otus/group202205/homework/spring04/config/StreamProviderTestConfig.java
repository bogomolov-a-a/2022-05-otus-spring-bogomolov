package ru.otus.group202205.homework.spring04.config;

import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.group202205.homework.spring04.service.ServiceConfig;

@Configuration
@Import({ServiceConfig.class, MessageSourceAutoConfiguration.class})
public class StreamProviderTestConfig {

}
