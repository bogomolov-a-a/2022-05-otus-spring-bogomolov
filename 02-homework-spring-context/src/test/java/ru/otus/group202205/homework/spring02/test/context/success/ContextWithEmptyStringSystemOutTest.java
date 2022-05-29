package ru.otus.group202205.homework.spring02.test.context.success;

import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.group202205.homework.spring02.test.context.config.TestAppConfig;
import ru.otus.group202205.homework.spring02.test.context.success.initializer.ContextWithEmptyStringSystemOutApplicationContextInitializer;
import ru.otus.group202205.homework.spring02.test.util.WithSystemOutTest;

@DisplayName("Like ContextDeployTest, only use empty strings in user input,"
    + " scanner wait for non empty sting for test processing")
@ContextConfiguration(classes = TestAppConfig.class,
    initializers = ContextWithEmptyStringSystemOutApplicationContextInitializer.class)
public class ContextWithEmptyStringSystemOutTest extends WithSystemOutTest {

  public ContextWithEmptyStringSystemOutTest() {
    super(false);
  }
}
