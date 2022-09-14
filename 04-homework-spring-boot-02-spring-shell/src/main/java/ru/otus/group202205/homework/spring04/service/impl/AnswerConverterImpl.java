package ru.otus.group202205.homework.spring04.service.impl;

import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring04.config.properties.LocaleProperties;
import ru.otus.group202205.homework.spring04.model.Answer;
import ru.otus.group202205.homework.spring04.service.AnswerConverter;

@Service
@RequiredArgsConstructor

public class AnswerConverterImpl implements AnswerConverter {

  private final MessageSource messageSource;
  private final LocaleProperties localeProperties;

  @Override
  public String convertToString(List<Answer> answers) {
    Locale locale = localeProperties.getCurrentLocale();
    StringBuilder builder = new StringBuilder(messageSource.getMessage("answer-service.possible-answer-head-text",
        null,
        locale)).append(System.lineSeparator());
    int answerCount = 1;
    for (Answer answer : answers) {
      builder
          .append(messageSource.getMessage("answer-service.possible-answer-variant-text",
              new Object[]{answerCount++, answer.getStatement()},
              locale))
          .append(System.lineSeparator());
    }
    return builder.toString();
  }

}
