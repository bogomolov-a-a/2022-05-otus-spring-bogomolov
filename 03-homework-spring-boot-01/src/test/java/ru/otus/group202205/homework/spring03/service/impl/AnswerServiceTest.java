package ru.otus.group202205.homework.spring03.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.group202205.homework.spring03.config.properties.LocaleProperties;
import ru.otus.group202205.homework.spring03.config.properties.PropertiesConfiguration;
import ru.otus.group202205.homework.spring03.config.properties.QuestionAnswerProperties;
import ru.otus.group202205.homework.spring03.model.QuestionType;
import ru.otus.group202205.homework.spring03.service.AnswerConverter;
import ru.otus.group202205.homework.spring03.service.AnswerService;
import ru.otus.group202205.homework.spring03.service.IoService;
import ru.otus.group202205.homework.spring03.service.QuestionConverter;
import ru.otus.group202205.homework.spring03.service.QuestionService;
import ru.otus.group202205.homework.spring03.service.ServiceConfig;
import ru.otus.group202205.homework.spring03.service.StreamProvider;
import ru.otus.group202205.homework.spring03.service.TestSuiteService;

@SpringBootTest(classes = {ServiceConfig.class, PropertiesConfiguration.class, MessageSourceAutoConfiguration.class})
@DirtiesContext
class AnswerServiceTest {

  @MockBean
  private AnswerConverter answerConverter;
  @MockBean
  private QuestionConverter questionConverter;
  @MockBean
  private QuestionService questionService;
  @MockBean
  private StreamProvider streamProvider;
  @MockBean
  private IoService ioService;
  @MockBean
  private TestSuiteService testSuiteService;
  @Autowired
  private LocaleProperties localeProperties;
  @Autowired
  private QuestionAnswerProperties questionAnswerProperties;

  @Autowired
  private AnswerService answerService;

  @Test
  void shouldBeIsSuccessAnswerMessageForMultiselectQuestion() {
    List<Long> actualIndexes = List.of(1L,
        2L);
    List<Long> expectedIndexes = List.of(1L,
        2L);
    assertThat(checkConsoleMessageByAnswersEquals(
        QuestionType.MULTISELECT,
        actualIndexes,
        expectedIndexes)).isTrue();
  }

  @Test
  void shouldBeIsSuccessAnswerMessageForRegularQuestion() {
    List<Long> actualIndexes = List.of(1L);
    List<Long> expectedIndexes = List.of(1L);
    assertThat(checkConsoleMessageByAnswersEquals(
        QuestionType.REGULAR,
        actualIndexes,
        expectedIndexes)).isTrue();
  }

  @Test
  void shouldBeIsFailAnswerMessageForRegularQuestion() {
    List<Long> actualIndexes = List.of(1L,
        2L);
    List<Long> expectedIndexes = List.of(1L);
    assertThat(checkConsoleMessageByAnswersEquals(
        QuestionType.REGULAR,
        actualIndexes,
        expectedIndexes)).isFalse();
  }

  @Test
  void shouldBeIsFailAnswerMessageForMultiselectQuestion() {
    List<Long> actualIndexes = List.of(1L,
        2L);
    List<Long> expectedIndexes = List.of(1L,
        3L);
    assertThat(checkConsoleMessageByAnswersEquals(
        QuestionType.MULTISELECT,
        actualIndexes,
        expectedIndexes)).isFalse();
  }

  @Test
  void shouldBeIsSuccessLevelAnswerMessageForMultiselectQuestion() {
    List<Long> actualIndexes = List.of(1L,
        2L,
        3L);
    List<Long> expectedIndexes = List.of(1L,
        2L,
        3L,
        4L,
        5L);
    assertThat(checkConsoleMessageByAnswersEquals(QuestionType.MULTISELECT,
        actualIndexes,
        expectedIndexes)).isTrue();
  }

  boolean checkConsoleMessageByAnswersEquals(QuestionType questionType, List<Long> actualIndexes,
      List<Long> expectedIndexes) {
    questionAnswerProperties.setMultiSelectAnswerLevelInPercent(0.6f);
    return answerService.examineQuestionAnswers(questionType,
        actualIndexes,
        expectedIndexes);
  }
}
