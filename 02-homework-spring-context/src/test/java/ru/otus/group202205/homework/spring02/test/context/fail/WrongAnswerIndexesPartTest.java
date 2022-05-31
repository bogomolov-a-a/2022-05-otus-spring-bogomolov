package ru.otus.group202205.homework.spring02.test.context.fail;

import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.group202205.homework.spring02.exception.QuestionTransformException;
import ru.otus.group202205.homework.spring02.test.context.config.TestAppConfig;
import ru.otus.group202205.homework.spring02.test.context.fail.initializer.WrongAnswerIndexesPartTestApplicationContextInitializer;
import ru.otus.group202205.homework.spring02.test.util.WithSystemOutTest;

@DisplayName("Check wrong answer indexes part(for example 'a')  case")
@ContextConfiguration(classes = TestAppConfig.class, initializers = WrongAnswerIndexesPartTestApplicationContextInitializer.class)
public class WrongAnswerIndexesPartTest extends WithSystemOutTest {

  public WrongAnswerIndexesPartTest() {
    super(true);
    setExceptionClass(QuestionTransformException.class);
  }
}
