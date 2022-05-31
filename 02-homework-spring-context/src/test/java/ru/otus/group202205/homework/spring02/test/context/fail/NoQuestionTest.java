package ru.otus.group202205.homework.spring02.test.context.fail;

import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.group202205.homework.spring02.exception.QuestionTransformException;
import ru.otus.group202205.homework.spring02.test.context.config.TestAppConfig;
import ru.otus.group202205.homework.spring02.test.context.fail.initializer.NoQuestionTestApplicationContextInitializer;
import ru.otus.group202205.homework.spring02.test.util.WithSystemOutTest;

@DisplayName("Check no present question lines in question.csv case")
@ContextConfiguration(classes = TestAppConfig.class, initializers = NoQuestionTestApplicationContextInitializer.class)
public class NoQuestionTest extends WithSystemOutTest {

  public NoQuestionTest() {
    super(true);
    setExceptionClass(QuestionTransformException.class);
  }
}
