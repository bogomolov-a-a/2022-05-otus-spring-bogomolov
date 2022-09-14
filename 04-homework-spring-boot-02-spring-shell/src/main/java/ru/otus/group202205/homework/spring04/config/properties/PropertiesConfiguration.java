package ru.otus.group202205.homework.spring04.config.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfiguration {

  @Bean
  public LocaleProperties localeProperties() {
    return new LocaleProperties();
  }

  @Bean
  public QuestionAnswerProperties questionAnswerProperties() {
    return new QuestionAnswerProperties();
  }

  @Bean
  public QuestionListProperties questionListProperties() {
    return new QuestionListProperties();
  }

  @Bean
  public TestSuiteProperties testSuiteProperties() {
    return new TestSuiteProperties();
  }

}
