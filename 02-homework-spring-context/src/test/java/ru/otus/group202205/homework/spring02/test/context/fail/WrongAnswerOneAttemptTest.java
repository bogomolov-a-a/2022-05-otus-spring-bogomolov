package ru.otus.group202205.homework.spring02.test.context.fail;

import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.group202205.homework.spring02.exception.TestFailedException;
import ru.otus.group202205.homework.spring02.test.context.config.TestAppConfig;
import ru.otus.group202205.homework.spring02.test.context.fail.initializer.WrongAnswerOneAttemptTestApplicationContextInitializer;
import ru.otus.group202205.homework.spring02.test.util.WithSystemOutTest;

@DisplayName("Check wrong answer and test fail in one attempt case")
@ContextConfiguration(classes = TestAppConfig.class, initializers = WrongAnswerOneAttemptTestApplicationContextInitializer.class)
public class WrongAnswerOneAttemptTest extends WithSystemOutTest {


  public WrongAnswerOneAttemptTest() {
    super(true);
    setExceptionClass(TestFailedException.class);
  }
}
