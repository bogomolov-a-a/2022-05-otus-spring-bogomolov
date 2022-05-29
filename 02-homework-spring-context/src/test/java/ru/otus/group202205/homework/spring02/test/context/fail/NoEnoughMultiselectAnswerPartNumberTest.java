package ru.otus.group202205.homework.spring02.test.context.fail;

import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.group202205.homework.spring02.exception.QuestionTransformException;
import ru.otus.group202205.homework.spring02.test.context.config.TestAppConfig;
import ru.otus.group202205.homework.spring02.test.context.fail.initializer.NoEnoughMultiselectAnswerPartNumberTestApplicationContextInitializer;
import ru.otus.group202205.homework.spring02.test.util.WithSystemOutTest;

@DisplayName("Check multiselect question no enough answer indexes case")
@ContextConfiguration(classes = TestAppConfig.class,
    initializers = NoEnoughMultiselectAnswerPartNumberTestApplicationContextInitializer.class)
public class NoEnoughMultiselectAnswerPartNumberTest extends WithSystemOutTest {

  public NoEnoughMultiselectAnswerPartNumberTest() {
    super(true);
    setExceptionClass(QuestionTransformException.class);
  }
}
