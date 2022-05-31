package ru.otus.group202205.homework.spring02.util.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring02.dao.impl.QuestionDaoCsv;
import ru.otus.group202205.homework.spring02.util.QuestionLinesReader;

@Component
public class QuestionLinesReaderCsv implements QuestionLinesReader {

  private static final String NOT_FOUND_RESOURCE_MESSAGE_TEMPLATE =
      "Question list not found at '%s'. Please check resource name and try again!";
  private static final String DURING_READ_QUESTION_LIST_ERROR_MESSAGE_TEMPLATE =
      "During read question list occurred exception. Cause: '%s'.";

  @Override
  public List<String> readQuestionLineFromCsv(String csvLocation) {
    try (InputStream questionListInputStream =
        QuestionDaoCsv.class.getClassLoader().getResourceAsStream(csvLocation);
        InputStreamReader questionListInputStreamReader = new InputStreamReader(
            Objects.requireNonNull(questionListInputStream,
                String.format(NOT_FOUND_RESOURCE_MESSAGE_TEMPLATE, csvLocation)));
        BufferedReader questionListInputStreamBufferedReader =
            new BufferedReader(questionListInputStreamReader)) {
      return questionListInputStreamBufferedReader.lines().collect(Collectors.toUnmodifiableList());
    } catch (IOException | NullPointerException e) {
      throw new IllegalStateException(
          String.format(DURING_READ_QUESTION_LIST_ERROR_MESSAGE_TEMPLATE, e.getMessage()));
    }
  }

}
