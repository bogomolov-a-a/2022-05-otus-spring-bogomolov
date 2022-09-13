package ru.otus.group202205.homework.spring04.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.FileNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.group202205.homework.spring04.config.properties.LocaleProperties;
import ru.otus.group202205.homework.spring04.config.properties.PropertiesConfiguration;
import ru.otus.group202205.homework.spring04.config.properties.QuestionListProperties;
import ru.otus.group202205.homework.spring04.dao.DaoConfig;
import ru.otus.group202205.homework.spring04.dao.QuestionDao;
import ru.otus.group202205.homework.spring04.dao.QuestionLinesReader;
import ru.otus.group202205.homework.spring04.dao.QuestionLinesTransformer;

@SpringBootTest(classes = {DaoConfig.class, PropertiesConfiguration.class, MessageSourceAutoConfiguration.class})
@DirtiesContext
class QuestionLinesReaderCsvTest {

  @MockBean
  private QuestionLinesTransformer questionLinesTransformer;
  @MockBean
  private QuestionDao questionDao;
  @Autowired
  private LocaleProperties localeProperties;
  @Autowired
  private QuestionListProperties questionListProperties;
  @Autowired
  private QuestionLinesReader questionLinesReader;
  @Autowired
  private MessageSource messageSource;

  @Test
  void shouldReadRussianQuestionsSuccess() {
    List<String> actualResult = shouldReadLocalizedQuestionSuccess("ru",
        "unit-test/questions/%s.csv");
    assertThat(actualResult)
        .asList()
        .containsOnly("В каком году был запущен первый искусственный спутник Земли?,regular,1,1957,1961,1977",
            "Какие виды приматов являются человекообразными обезьянами?,multiselect,1;2,Pongo pygmaeus,Pongo abelii,Cebus capucinus",
            "В каком году был запущен Вояджер-1?,regular,3,1957,1961,1977",
            "В каком году был запущен первый пилотируемый искусственный спутник?,regular,2,1957,1961,1977",
            "К какому подсемейству кошачьих относится манул?,regular,1,малые кошки,большие кошки");
  }

  @Test
  void shouldThrowsIllegalArgumentExceptionByLocationNull() {
    questionListProperties.setLocation(null);
    localeProperties.setLanguage("en");
    assertThatThrownBy(questionLinesReader::readQuestionLines)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(messageSource.getMessage("question-reader.not-specified-question-list-location-field",
            null,
            localeProperties.getCurrentLocale()));
  }

  @Test
  void shouldThrowsIllegalArgumentExceptionByLocaleNull() {
    questionListProperties.setLocation("unit-test/success-transform-questions/question-%s.csv");
    localeProperties.setLanguage(null);
    assertThatThrownBy(questionLinesReader::readQuestionLines)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("not specified locale language for questions!");
  }

  @Test
  void shouldThrowsIllegalStateExceptionCausingFileNotFoundByResourceNotFound() {
    String notExistQuestionLocation = "unit-test/question/%s-not-exists.csv";
    questionListProperties.setLocation(notExistQuestionLocation);
    localeProperties.setLanguage("en");
    assertThatThrownBy(questionLinesReader::readQuestionLines)
        .isInstanceOf(IllegalStateException.class)
        .hasCauseInstanceOf(FileNotFoundException.class)
        .hasMessage(messageSource.getMessage("question-reader.can-not-read-question-list-error-message",
            new Object[]{String.format("class path resource [" + notExistQuestionLocation + "] cannot be opened because it does not exist",
                localeProperties.getLanguage())},
            localeProperties.getCurrentLocale()));
  }

  @Test
  void shouldReadEmptyQuestionList() {
    List<String> actualResult = shouldReadLocalizedQuestionSuccess("en",
        "unit-test/no-question/questions.csv");
    assertThat(actualResult)
        .asList()
        .isEmpty();
  }

  private List<String> shouldReadLocalizedQuestionSuccess(String localeLanguage, String location) {
    questionListProperties.setLocation(location);
    localeProperties.setLanguage(localeLanguage);
    return questionLinesReader.readQuestionLines();
  }

}
