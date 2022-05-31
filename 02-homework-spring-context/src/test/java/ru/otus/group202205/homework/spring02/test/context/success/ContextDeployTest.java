package ru.otus.group202205.homework.spring02.test.context.success;

import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.group202205.homework.spring02.config.AppConfig;
import ru.otus.group202205.homework.spring02.test.context.success.initializer.SuccessTestApplicationContextInitializer;
import ru.otus.group202205.homework.spring02.test.util.WithSystemOutTest;

@DisplayName("App context test, use default AppConfig class as application context configuration")
@ContextConfiguration(classes = AppConfig.class, initializers = SuccessTestApplicationContextInitializer.class)
public class ContextDeployTest extends WithSystemOutTest {

  public ContextDeployTest() {
    super(false);
  }

}
