package ru.otus.group202205.homework.spring02.test.context.fail;

import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.group202205.homework.spring02.exception.QuestionTransformException;
import ru.otus.group202205.homework.spring02.test.context.config.TestAppConfig;
import ru.otus.group202205.homework.spring02.test.context.fail.initializer.NoQuestionTypeTestApplicationContextInitializer;
import ru.otus.group202205.homework.spring02.test.util.WithSystemOutTest;

@DisplayName("Check no present question lines in question.csv case")
@ContextConfiguration(classes = TestAppConfig.class, initializers = NoQuestionTypeTestApplicationContextInitializer.class)
public class NoQuestionTypeTest extends WithSystemOutTest {

  public NoQuestionTypeTest() {
    super(true);
    setExceptionClass(QuestionTransformException.class);
  }
}
