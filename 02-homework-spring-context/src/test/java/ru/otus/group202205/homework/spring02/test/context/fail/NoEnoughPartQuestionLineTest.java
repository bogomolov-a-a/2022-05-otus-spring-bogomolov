package ru.otus.group202205.homework.spring02.test.context.fail;

import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.group202205.homework.spring02.exception.QuestionTransformException;
import ru.otus.group202205.homework.spring02.test.context.config.TestAppConfig;
import ru.otus.group202205.homework.spring02.test.context.fail.initializer.NoEnoughPartQuestionLineTestApplicationContextInitializer;
import ru.otus.group202205.homework.spring02.test.util.WithSystemOutTest;

@DisplayName("Check no enough part in question line case")
@ContextConfiguration(classes = TestAppConfig.class, initializers = NoEnoughPartQuestionLineTestApplicationContextInitializer.class)
public class NoEnoughPartQuestionLineTest extends WithSystemOutTest {

  public NoEnoughPartQuestionLineTest() {
    super(true);
    setExceptionClass(QuestionTransformException.class);
  }
}
