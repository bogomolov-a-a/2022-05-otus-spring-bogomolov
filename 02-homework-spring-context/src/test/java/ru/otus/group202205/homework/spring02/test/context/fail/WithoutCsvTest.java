package ru.otus.group202205.homework.spring02.test.context.fail;

import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.group202205.homework.spring02.test.context.config.TestAppConfig;
import ru.otus.group202205.homework.spring02.test.context.fail.initializer.WithoutCsvTestApplicationContextInitializer;
import ru.otus.group202205.homework.spring02.test.util.WithSystemOutTest;

@DisplayName("Check no present question.csv case")
@ContextConfiguration(classes = TestAppConfig.class, initializers = WithoutCsvTestApplicationContextInitializer.class)
public class WithoutCsvTest extends WithSystemOutTest {

  public WithoutCsvTest() {
    super(true);
    setExceptionClass(IllegalStateException.class);
  }

}
