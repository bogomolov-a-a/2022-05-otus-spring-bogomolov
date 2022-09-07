package ru.otus.group202205.homework.spring03.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.group202205.homework.spring03.config.properties.LocaleProperties;
import ru.otus.group202205.homework.spring03.config.properties.PropertiesConfiguration;
import ru.otus.group202205.homework.spring03.config.properties.QuestionListProperties;
import ru.otus.group202205.homework.spring03.dao.DaoConfig;
import ru.otus.group202205.homework.spring03.model.Answer;
import ru.otus.group202205.homework.spring03.model.Question;
import ru.otus.group202205.homework.spring03.model.QuestionType;

@SpringBootTest(classes = {DaoConfig.class, PropertiesConfiguration.class, MessageSourceAutoConfiguration.class})
@DirtiesContext
class QuestionDaoCsvTest {

  @Autowired
  private QuestionDaoCsv questionDao;
  @Autowired
  private LocaleProperties localeProperties;
  @Autowired
  private QuestionListProperties questionListProperties;

  @Test
  void shouldGetQuestionCorrect() {
    localeProperties.setLanguage("ru");
    questionListProperties.setLocation("unit-test/success-transform-questions/questions.csv");
    List<Question> transformedQuestions = questionDao.getQuestions();
    assertThat(transformedQuestions)
        .asList()
        .isNotEmpty()
        .containsOnly(createExceptedQuestions());
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
