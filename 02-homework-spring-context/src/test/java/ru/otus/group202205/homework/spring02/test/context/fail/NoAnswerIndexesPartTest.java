package ru.otus.group202205.homework.spring02.test.context.fail;

import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.group202205.homework.spring02.exception.QuestionTransformException;
import ru.otus.group202205.homework.spring02.test.context.config.TestAppConfig;
import ru.otus.group202205.homework.spring02.test.context.fail.initializer.NoAnswerIndexesPartTestApplicationContextInitializer;
import ru.otus.group202205.homework.spring02.test.util.WithSystemOutTest;

@DisplayName("Check answer index part missed case")
@ContextConfiguration(classes = TestAppConfig.class,
    initializers = NoAnswerIndexesPartTestApplicationContextInitializer.class)
public class NoAnswerIndexesPartTest extends WithSystemOutTest {

  public NoAnswerIndexesPartTest() {
    super(true);
    setExceptionClass(QuestionTransformException.class);
  }
}
