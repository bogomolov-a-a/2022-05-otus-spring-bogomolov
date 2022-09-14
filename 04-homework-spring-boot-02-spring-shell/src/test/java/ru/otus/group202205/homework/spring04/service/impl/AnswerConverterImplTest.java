package ru.otus.group202205.homework.spring04.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.group202205.homework.spring04.config.properties.LocaleProperties;
import ru.otus.group202205.homework.spring04.config.properties.PropertiesConfiguration;
import ru.otus.group202205.homework.spring04.model.Answer;
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
class AnswerConverterImplTest {

  @MockBean
  private AnswerService answerService;
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
  private AnswerConverter answerConverter;

  @Test
  void shouldBeConvertTwoVariantOneAnswerWithHeader() {
    localeProperties.setLanguage("en");
    List<Answer> testableAnswers = List.of(new Answer("answer1"),
        new Answer("answer2"));
    String actualAnswerVariantsString = answerConverter.convertToString(testableAnswers);
    String expectedAnswerVariantsString =
        "Possible answers:" + System.lineSeparator() + "Variant 1. answer1" + System.lineSeparator() + "Variant 2. answer2" + System.lineSeparator();
    assertThat(actualAnswerVariantsString).isEqualTo(
        expectedAnswerVariantsString);
  }

}
