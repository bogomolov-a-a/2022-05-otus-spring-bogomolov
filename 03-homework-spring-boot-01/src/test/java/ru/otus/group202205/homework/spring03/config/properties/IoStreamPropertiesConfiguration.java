package ru.otus.group202205.homework.spring03.config.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IoStreamPropertiesConfiguration {

  @Bean
  public IoStreamTestProperties ioStreamTestProperties() {
    return new IoStreamTestProperties();
  }
}
