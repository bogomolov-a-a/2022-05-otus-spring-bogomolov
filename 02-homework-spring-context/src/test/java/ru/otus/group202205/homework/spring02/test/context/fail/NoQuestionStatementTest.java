package ru.otus.group202205.homework.spring02.test.context.fail;

import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.group202205.homework.spring02.exception.QuestionTransformException;
import ru.otus.group202205.homework.spring02.test.context.config.TestAppConfig;
import ru.otus.group202205.homework.spring02.test.context.fail.initializer.NoQuestionStatementTestApplicationContextInitializer;
import ru.otus.group202205.homework.spring02.test.util.WithSystemOutTest;

@DisplayName("Check no present question statement in question line case")
@ContextConfiguration(classes = TestAppConfig.class, initializers = NoQuestionStatementTestApplicationContextInitializer.class)
public class NoQuestionStatementTest extends WithSystemOutTest {

  public NoQuestionStatementTest() {
    super(true);
    setExceptionClass(QuestionTransformException.class);
  }
}
