package ru.otus.group202205.homework.spring03.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring03.config.properties.LocaleProperties;
import ru.otus.group202205.homework.spring03.model.Question;
import ru.otus.group202205.homework.spring03.service.QuestionConverter;

@Service
@RequiredArgsConstructor

public class QuestionConverterImpl implements QuestionConverter {

  private final MessageSource messageSource;
  private final LocaleProperties localeProperties;

  @Override
  public String convertToString(Question question, int questionIndex) {
    return messageSource.getMessage("question-service.question-statement",
        new Object[]{questionIndex, question.getType(), question.getStatement()},
        localeProperties.getCurrentLocale());
  }
}
