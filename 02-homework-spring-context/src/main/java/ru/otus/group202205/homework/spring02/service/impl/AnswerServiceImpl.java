package ru.otus.group202205.homework.spring02.service.impl;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring02.model.Answer;
import ru.otus.group202205.homework.spring02.model.QuestionType;
import ru.otus.group202205.homework.spring02.service.AnswerService;
import ru.otus.group202205.homework.spring02.util.impl.ConsoleUtil;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

  private static final String QUESTION_ANSWER_STATEMENTS = "Possible answers:\n";
  private static final String QUESTION_ANSWER_STATEMENT = "%d. %s";
  private static final String WRONG_ANSWER_FOR_REGULAR_QUESTION_MESSAGE =
      "For this question correct variant: %d, but received %d";
  private static final String STUDENT_ANSWER_CORRECT_MESSAGE =
      "You answer for this question is correct!";
  private static final String WRONG_ANSWER_FOR_MULTISELECT_QUESTION_MESSAGE =
      "For this question correct variants: %s, but received %s";

  private final ConsoleUtil consoleUtil;

  @Override
  public void printPossibleAnswers(List<Answer> answers) {
    consoleUtil.printMessageToConsole(QUESTION_ANSWER_STATEMENTS);
    int answerCount = 1;
    for (Answer answer : answers) {
      consoleUtil.printMessageToConsole(
          String.format(QUESTION_ANSWER_STATEMENT, answerCount++, answer.getStatement()));
    }
  }

  @Override
  public boolean examineQuestionAnswers(QuestionType questionType,
      List<Long> studentAnswerIndexes,
      List<Long> exceptedAnswerIndexes,
      float multiselectCorrectAnswerLevel) {
    boolean result = (QuestionType.REGULAR.equals(questionType)
        ? examineRegularQuestionAnswer(studentAnswerIndexes, exceptedAnswerIndexes)
        : examineMultiselectQuestionAnswers(studentAnswerIndexes,
            exceptedAnswerIndexes,
            multiselectCorrectAnswerLevel));
    if (result) {
      consoleUtil.printMessageToConsole(STUDENT_ANSWER_CORRECT_MESSAGE);
    }
    return result;
  }

  private boolean examineRegularQuestionAnswer(List<Long> studentAnswerIndexes,
      List<Long> exceptedAnswerIndexes) {
    boolean result = Objects.equals(exceptedAnswerIndexes, studentAnswerIndexes);
    if (!result) {
      consoleUtil.printMessageToConsole(String.format(WRONG_ANSWER_FOR_REGULAR_QUESTION_MESSAGE,
          exceptedAnswerIndexes.get(0), studentAnswerIndexes.get(0)));
    }
    return result;
  }

  private boolean examineMultiselectQuestionAnswers(List<Long> studentAnswerIndexes,
      List<Long> exceptedAnswerIndexes,
      float multiselectCorrectAnswerLevel) {
    boolean result = examineMultiselectQuestionCorrectAnswerLevel(studentAnswerIndexes.size(),
        exceptedAnswerIndexes.size(),
        multiselectCorrectAnswerLevel);
    if (!result) {
      consoleUtil.printMessageToConsole(String.format(WRONG_ANSWER_FOR_MULTISELECT_QUESTION_MESSAGE,
          exceptedAnswerIndexes, studentAnswerIndexes));
    }
    return result;
  }

  private boolean examineMultiselectQuestionCorrectAnswerLevel(int studentAnswerCount,
      int exceptedAnswerCount,
      float multiselectCorrectAnswerLevel) {
    return Float.compare((float) studentAnswerCount / exceptedAnswerCount,
        multiselectCorrectAnswerLevel) >= 0;
  }
}
