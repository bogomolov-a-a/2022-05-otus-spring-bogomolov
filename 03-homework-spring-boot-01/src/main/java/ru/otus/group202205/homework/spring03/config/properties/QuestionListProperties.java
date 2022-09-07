package ru.otus.group202205.homework.spring03.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("app.question-list")
public class QuestionListProperties {

  private String location;

}
