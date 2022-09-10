package ru.otus.group202205.homework.spring03.it;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.group202205.homework.spring03.config.TestSuiteServiceImplTestConfig;
import ru.otus.group202205.homework.spring03.exception.TestFailedException;
import ru.otus.group202205.homework.spring03.service.TestSuiteService;

@SpringBootTest(classes = TestSuiteServiceImplTestConfig.class)
@ActiveProfiles("fail-one-more-attempt")
class FailOneMoreAttemptTestSuiteServiceImplTest {

  @Autowired
  private TestSuiteService testSuiteService;

  @Test
  void shouldBeStudentTestingFailOnOneMoreAttempt() {
    assertThatCode(() -> testSuiteService.performStudentTesting()).isInstanceOf(TestFailedException.class);
  }
}