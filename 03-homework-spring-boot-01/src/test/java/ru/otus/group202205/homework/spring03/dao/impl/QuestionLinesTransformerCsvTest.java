package ru.otus.group202205.homework.spring03.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.group202205.homework.spring03.config.properties.LocaleProperties;
import ru.otus.group202205.homework.spring03.config.properties.PropertiesConfiguration;
import ru.otus.group202205.homework.spring03.config.properties.QuestionListProperties;
import ru.otus.group202205.homework.spring03.dao.DaoConfig;
import ru.otus.group202205.homework.spring03.dao.QuestionDao;
import ru.otus.group202205.homework.spring03.dao.QuestionLinesReader;
import ru.otus.group202205.homework.spring03.dao.QuestionLinesTransformer;
import ru.otus.group202205.homework.spring03.exception.QuestionTransformException;
import ru.otus.group202205.homework.spring03.model.Answer;
import ru.otus.group202205.homework.spring03.model.Question;
import ru.otus.group202205.homework.spring03.model.QuestionType;

@SpringBootTest(classes = {DaoConfig.class, PropertiesConfiguration.class, MessageSourceAutoConfiguration.class})
@DirtiesContext
class QuestionLinesTransformerCsvTest {

  @MockBean
  private QuestionDao questionDao;
  @Autowired
  private LocaleProperties localeProperties;
  @Autowired
  private QuestionListProperties questionListProperties;
  @Autowired
  private QuestionLinesTransformer questionLinesTransformer;
  @Autowired
  private QuestionLinesReader questionLinesReader;
  @Autowired
  private MessageSource messageSource;

  @Test
  void shouldReadQuestionsSuccess() {
    List<String> lines = shouldReadLinesFromCsv("unit-test/success-transform-questions/questions.csv"
    );
    List<Question> transformedQuestions = questionLinesTransformer.transformQuestionInputToQuestion(lines);
    assertThat(transformedQuestions)
        .asList()
        .isNotEmpty()
        .containsOnly(createExceptedQuestions());
  }

  @Test
  void shouldThrowsQuestionTransformExceptionByLinesIsEmpty() {
    assertThatThrownBy(() -> questionLinesTransformer.transformQuestionInputToQuestion(Collections.emptyList()))
        .isInstanceOf(QuestionTransformException.class)
        .hasMessage(messageSource.getMessage("question-transformer.empty-question-list-error-message",
            null,
            localeProperties.getCurrentLocale()));
  }

  @Test
  void shouldThrowsQuestionTransformExceptionByMinimumQuestionLineParts() {
    List<String> lines = shouldReadLinesFromCsv("unit-test/no-enough-parts/questions.csv"
    );
    assertThatThrownBy(() -> questionLinesTransformer.transformQuestionInputToQuestion(lines))
        .isInstanceOf(QuestionTransformException.class)
        .hasMessage(messageSource.getMessage("question-transformer.not-enough-part-number-error-message",
            new Object[]{QuestionType.DEFAULT.getType(), QuestionType.DEFAULT.getQuestionLineMinimumPartNumber(), 1},
            localeProperties.getCurrentLocale()));
  }

  @Test
  void shouldThrowsQuestionTransformExceptionByNoQuestionStatement() {
    List<String> lines = shouldReadLinesFromCsv("unit-test/no-question-statement/questions.csv"
    );
    assertThatThrownBy(() -> questionLinesTransformer.transformQuestionInputToQuestion(lines))
        .isInstanceOf(QuestionTransformException.class)
        .hasMessage(messageSource.getMessage("question-transformer.not-found-required-part-error-message",
            new Object[]{QuestionLinesTransformerCsv.STATEMENT_PART_NAME, QuestionLinesTransformerCsv.QUESTION_STATEMENT_PART_INDEX},
            localeProperties.getCurrentLocale()));
  }

  @Test
  void shouldThrowsQuestionTransformExceptionByNoQuestionType() {
    List<String> lines = shouldReadLinesFromCsv("unit-test/no-question-type/questions.csv"
    );
    assertThatThrownBy(() -> questionLinesTransformer.transformQuestionInputToQuestion(lines))
        .isInstanceOf(QuestionTransformException.class)
        .hasMessage(messageSource.getMessage("question-transformer.not-found-required-part-error-message",
            new Object[]{QuestionLinesTransformerCsv.TYPE_PART_NAME, QuestionLinesTransformerCsv.QUESTION_TYPE_PART_INDEX},
            localeProperties.getCurrentLocale()));
  }

  @Test
  void shouldThrowsQuestionTransformExceptionByWrongQuestionType() {
    List<String> lines = shouldReadLinesFromCsv("unit-test/with-wrong-question-type/questions.csv"
    );
    assertThatThrownBy(() -> questionLinesTransformer.transformQuestionInputToQuestion(lines))
        .isInstanceOf(QuestionTransformException.class)
        .hasMessage(messageSource.getMessage("question-transformer.not-found-required-part-error-message",
            new Object[]{QuestionLinesTransformerCsv.TYPE_PART_NAME, QuestionLinesTransformerCsv.QUESTION_TYPE_PART_INDEX},
            localeProperties.getCurrentLocale()));
  }

  @Test
  void shouldThrowsQuestionTransformExceptionByNoAnswerIndexesQuestionLinePart() {
    List<String> lines = shouldReadLinesFromCsv("unit-test/no-answer-indexes-part/questions.csv"
    );
    assertThatThrownBy(() -> questionLinesTransformer.transformQuestionInputToQuestion(lines))
        .isInstanceOf(QuestionTransformException.class)
        .hasMessage(messageSource.getMessage("question-transformer.not-found-required-part-error-message",
            new Object[]{QuestionLinesTransformerCsv.CORRECT_ANSWER_INDEXES_PART_NAME, QuestionLinesTransformerCsv.QUESTION_CORRECT_ANSWER_INDEXES_PART_INDEX},
            localeProperties.getCurrentLocale()));
  }

  @Test
  void shouldThrowsQuestionTransformExceptionByWrongAnswerIndexesQuestionLinePart() {
    List<String> lines = shouldReadLinesFromCsv("unit-test/with-wrong-answer-indexes-part/questions.csv"
    );
    assertThatThrownBy(() -> questionLinesTransformer.transformQuestionInputToQuestion(lines))
        .isInstanceOf(QuestionTransformException.class)
        .hasMessage((messageSource.getMessage("question-transformer.answer-indexes-must-be-only-number-error-message",
            new Object[]{QuestionType.REGULAR.getType(), "For input string: \"a\""},
            localeProperties.getCurrentLocale())));
  }

  @Test
  void shouldThrowsQuestionTransformExceptionByNoEnoughMultiselectAnswerPartNumber() {
    List<String> lines = shouldReadLinesFromCsv("unit-test/no-enough-multiselect-answer-part-number/questions.csv"
    );
    assertThatThrownBy(() -> questionLinesTransformer.transformQuestionInputToQuestion(lines))
        .isInstanceOf(QuestionTransformException.class)
        .hasMessage(messageSource.getMessage("question-transformer.not-enough-correct-answer-indexes-error-message",
            new Object[]{QuestionType.MULTISELECT.getType(), QuestionType.MULTISELECT.getQuestionLineMinimumPartNumber(), 1},
            localeProperties.getCurrentLocale()));
  }

  @Test
  void shouldThrowsQuestionTransformExceptionByNoAnswerStatement() {
    List<String> lines = shouldReadLinesFromCsv("unit-test/no-answer-statement/questions.csv"
    );
    assertThatThrownBy(() -> questionLinesTransformer.transformQuestionInputToQuestion(lines))
        .isInstanceOf(QuestionTransformException.class)
        .hasMessage(messageSource.getMessage("question-transformer.not-found-required-answer-part-error-message",
            new Object[]{QuestionLinesTransformerCsv.STATEMENT_PART_NAME},
            localeProperties.getCurrentLocale()));
  }

  private List<String> shouldReadLinesFromCsv(String location) {
    questionListProperties.setLocation(location);
    localeProperties.setLanguage("en");
    return questionLinesReader.readQuestionLines();
  }

  private Question[] createExceptedQuestions() {
    Question question1 = new Question("What types of apes are anthropoids?",
        QuestionType.MULTISELECT);
    question1
        .getCorrectAnswerIndexList()
        .addAll(Arrays.asList(1L,
            2L));
    question1
        .getAnswers()
        .addAll(Arrays.asList(new Answer("Pongo pygmaeus"),
            new Answer("Pongo abelii"),
            new Answer("Cebus capucinus")));
    Question question2 = new Question("In what year was Voyadger-1 launched?",
        QuestionType.REGULAR);
    question2
        .getCorrectAnswerIndexList()
        .add(3L);
    question2
        .getAnswers()
        .addAll(Arrays.asList(new Answer("1957"),
            new Answer("1961"),
            new Answer("1977")));
    return new Question[]{question1, question2};
  }

}
