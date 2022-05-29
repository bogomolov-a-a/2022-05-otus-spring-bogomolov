package ru.otus.group202205.homework.spring02.test.context.fail;

import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.group202205.homework.spring02.exception.TestFailedException;
import ru.otus.group202205.homework.spring02.test.context.config.TestAppConfig;
import ru.otus.group202205.homework.spring02.test.context.fail.initializer.WrongTypingTwoAttemptTestApplicationContextInitializer;
import ru.otus.group202205.homework.spring02.test.util.WithSystemOutTest;

@DisplayName("Check wrong typing in two attempts and test fail case")
@ContextConfiguration(classes = TestAppConfig.class, initializers = WrongTypingTwoAttemptTestApplicationContextInitializer.class)
public class WrongTypingTwoAttemptTest extends WithSystemOutTest {

  public WrongTypingTwoAttemptTest() {
    super(true);
    setExceptionClass(TestFailedException.class);
  }
}
