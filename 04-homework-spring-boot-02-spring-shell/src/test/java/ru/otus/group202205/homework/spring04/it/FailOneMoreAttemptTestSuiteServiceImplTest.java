package ru.otus.group202205.homework.spring04.it;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.group202205.homework.spring04.config.TestSuiteServiceImplTestConfig;
import ru.otus.group202205.homework.spring04.config.properties.LocaleProperties;
import ru.otus.group202205.homework.spring04.exception.TestFailedException;
import ru.otus.group202205.homework.spring04.service.TestSuiteService;

@SpringBootTest(classes = TestSuiteServiceImplTestConfig.class)
@ActiveProfiles("fail-one-more-attempt")
@DirtiesContext
class FailOneMoreAttemptTestSuiteServiceImplTest {

  @Autowired
  private LocaleProperties localeProperties;
  @Autowired
  private TestSuiteService testSuiteService;

  @Test
  void shouldBeStudentTestingFailOnOneMoreAttempt() {
    localeProperties.setLanguage("ru");
    assertThatCode(() -> testSuiteService.performStudentTesting()).isInstanceOf(TestFailedException.class);
  }

}