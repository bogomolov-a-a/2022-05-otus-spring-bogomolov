package ru.otus.group202205.homework.spring03.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring03.config.properties.LocaleProperties;
import ru.otus.group202205.homework.spring03.config.properties.QuestionAnswerProperties;
import ru.otus.group202205.homework.spring03.model.QuestionType;
import ru.otus.group202205.homework.spring03.service.AnswerService;
import ru.otus.group202205.homework.spring03.service.IoService;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

  private final IoService ioService;
  private final QuestionAnswerProperties multiSelectQuestionQuestionAnswerProperties;
  private final MessageSource messageSource;
  private final LocaleProperties localeProperties;

  @Override
  public boolean examineQuestionAnswers(QuestionType questionType, List<Long> studentAnswerIndexes, List<Long> exceptedAnswerIndexes) {
    boolean result = (QuestionType.REGULAR.equals(questionType) ? examineRegularQuestionAnswer(studentAnswerIndexes,
        exceptedAnswerIndexes) : examineMultiselectQuestionAnswers(studentAnswerIndexes,
        exceptedAnswerIndexes,
        multiSelectQuestionQuestionAnswerProperties.getMultiSelectAnswerLevelInPercent()));
    if (result) {
      ioService.outputMessage(messageSource.getMessage("answer-service.user-correct-answer-message",
          null,
          localeProperties.getCurrentLocale()));
    }
    return result;
  }

  private boolean examineRegularQuestionAnswer(List<Long> studentAnswerIndexes, List<Long> exceptedAnswerIndexes) {
    boolean result = Objects.equals(exceptedAnswerIndexes,
        studentAnswerIndexes);
    if (!result) {
      ioService.outputMessage(messageSource.getMessage("answer-service.user-answer-missing-message",
          new Object[]{exceptedAnswerIndexes.get(0), studentAnswerIndexes},
          localeProperties.getCurrentLocale()));
    }
    return result;
  }

  private boolean examineMultiselectQuestionAnswers(List<Long> studentAnswerIndexes, List<Long> exceptedAnswerIndexes, float multiselectCorrectAnswerLevel) {
    boolean result = examineMultiselectQuestionCorrectAnswerLevel(studentAnswerIndexes,
        exceptedAnswerIndexes,
        multiselectCorrectAnswerLevel);
    if (!result) {
      ioService.outputMessage(messageSource.getMessage("answer-service.user-multiselect-answer-missing-message",
          new Object[]{exceptedAnswerIndexes, studentAnswerIndexes},
          localeProperties.getCurrentLocale()));
    }
    return result;
  }

  private boolean examineMultiselectQuestionCorrectAnswerLevel(List<Long> studentAnswers, List<Long> exceptedAnswers, float multiselectCorrectAnswerLevel) {
    boolean result = Objects.equals(studentAnswers,
        exceptedAnswers);
    if (result) {
      return true;
    }
    List<Long> rightStudentAnswerIndexes = studentAnswers
        .stream()
        .filter(exceptedAnswers::contains)
        .collect(Collectors.toUnmodifiableList());
    return Float.compare((float) rightStudentAnswerIndexes.size() / exceptedAnswers.size(),
        multiselectCorrectAnswerLevel) >= 0;
  }
}
