package ru.otus.group202205.homework.spring02.test.context.success.initializer;

import ru.otus.group202205.homework.spring02.test.util.BasicSystemInputApplicationContextInitializer;

public class ContextWithEmptyStringSystemOutApplicationContextInitializer extends BasicSystemInputApplicationContextInitializer {

  protected ContextWithEmptyStringSystemOutApplicationContextInitializer() {
    super("context-test-with-empty-string/application.properties");
  }
}
