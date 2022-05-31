package ru.otus.group202205.homework.spring02.test.context.fail;

import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.group202205.homework.spring02.exception.QuestionTransformException;
import ru.otus.group202205.homework.spring02.test.context.config.TestAppConfig;
import ru.otus.group202205.homework.spring02.test.context.fail.initializer.NoAnswerStatementTestApplicationContextInitializer;
import ru.otus.group202205.homework.spring02.test.util.WithSystemOutTest;

@DisplayName("Check answer statement missed case")
@ContextConfiguration(classes = TestAppConfig.class,
    initializers = NoAnswerStatementTestApplicationContextInitializer.class)
public class NoAnswerStatementTest extends WithSystemOutTest {

  public NoAnswerStatementTest() {
    super(true);
    setExceptionClass(QuestionTransformException.class);
  }
}
