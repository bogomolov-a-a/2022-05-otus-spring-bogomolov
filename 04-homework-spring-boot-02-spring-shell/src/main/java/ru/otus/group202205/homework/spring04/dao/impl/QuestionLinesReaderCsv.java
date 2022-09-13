package ru.otus.group202205.homework.spring04.dao.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring04.config.properties.LocaleProperties;
import ru.otus.group202205.homework.spring04.config.properties.QuestionListProperties;
import ru.otus.group202205.homework.spring04.dao.QuestionLinesReader;

@Component
@RequiredArgsConstructor
public class QuestionLinesReaderCsv implements QuestionLinesReader {

  private final MessageSource messageSource;
  private final QuestionListProperties questionListProperties;

  private final LocaleProperties localeProperties;

  @Override
  public List<String> readQuestionLines() {
    if (localeProperties.getLanguage() == null) {
      throw new IllegalArgumentException("not specified locale language for questions!");
    }
    if (questionListProperties.getLocation() == null) {
      throw new IllegalArgumentException(messageSource.getMessage("question-reader.not-specified-question-list-location-field",
          null,
          localeProperties.getCurrentLocale()));
    }
    String csvLocation = String.format(questionListProperties.getLocation(),
        localeProperties.getLanguage());
    try (InputStream questionListInputStream = new ClassPathResource(csvLocation).getInputStream();
        InputStreamReader questionListInputStreamReader = new InputStreamReader(questionListInputStream,
            StandardCharsets.UTF_8);
        BufferedReader questionListInputStreamBufferedReader = new BufferedReader(questionListInputStreamReader)) {
      return questionListInputStreamBufferedReader
          .lines()
          .collect(Collectors.toUnmodifiableList());
    } catch (IOException e) {
      throw new IllegalStateException(messageSource.getMessage("question-reader.can-not-read-question-list-error-message",
          new Object[]{e.getMessage()},
          localeProperties.getCurrentLocale()),
          e);
    }
  }

}
