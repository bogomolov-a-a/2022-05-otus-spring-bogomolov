package ru.otus.group202205.homework.spring02.test.context.fail;

import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.group202205.homework.spring02.exception.QuestionTransformException;
import ru.otus.group202205.homework.spring02.test.context.config.TestAppConfig;
import ru.otus.group202205.homework.spring02.test.context.fail.initializer.WrongQuestionTypeTestApplicationContextInitializer;
import ru.otus.group202205.homework.spring02.test.util.WithSystemOutTest;

@DisplayName("Check wrong question type or not present question type case")
@ContextConfiguration(classes = TestAppConfig.class, initializers = WrongQuestionTypeTestApplicationContextInitializer.class)
public class WrongQuestionTypeTest extends WithSystemOutTest {

  public WrongQuestionTypeTest() {
    super(true);
    setExceptionClass(QuestionTransformException.class);
  }
}
