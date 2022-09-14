package ru.otus.group202205.homework.spring04.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.group202205.homework.spring04.config.properties.LocaleProperties;
import ru.otus.group202205.homework.spring04.config.properties.PropertiesConfiguration;
import ru.otus.group202205.homework.spring04.model.Question;
import ru.otus.group202205.homework.spring04.model.QuestionType;
import ru.otus.group202205.homework.spring04.service.AnswerConverter;
import ru.otus.group202205.homework.spring04.service.AnswerService;
import ru.otus.group202205.homework.spring04.service.IoService;
import ru.otus.group202205.homework.spring04.service.QuestionConverter;
import ru.otus.group202205.homework.spring04.service.QuestionService;
import ru.otus.group202205.homework.spring04.service.ServiceConfig;
import ru.otus.group202205.homework.spring04.service.StreamProvider;
import ru.otus.group202205.homework.spring04.service.TestSuiteService;

@SpringBootTest(classes = {ServiceConfig.class, PropertiesConfiguration.class, MessageSourceAutoConfiguration.class})
@DirtiesContext
class QuestionConverterImplTest {

  @MockBean
  private AnswerService answerService;
  @MockBean
  private AnswerConverter answerConverter;
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
  private QuestionConverter questionConverter;

  @Test
  void shouldBeConvertRegularQuestionStatementWithNumberTwo() {
    localeProperties.setLanguage("en");
    Question question = new Question("In what year was Voyadger-1 launched?",
        QuestionType.REGULAR);
    String actualConvertedQuestionString = questionConverter.convertToString(question,
        2);
    assertThat(actualConvertedQuestionString).isEqualTo("Question number 2 (REGULAR): In what year was Voyadger-1 launched?");
  }

  @Test
  void shouldBeConvertMultiSelectQuestionStatementWithNumberTwo() {
    localeProperties.setLanguage("en");
    Question question = new Question("What types of apes are anthropoids?",
        QuestionType.MULTISELECT);
    String actualConvertedQuestionString = questionConverter.convertToString(question,
        1);
    assertThat(actualConvertedQuestionString).isEqualTo("Question number 1 (MULTISELECT): What types of apes are anthropoids?");
  }

}