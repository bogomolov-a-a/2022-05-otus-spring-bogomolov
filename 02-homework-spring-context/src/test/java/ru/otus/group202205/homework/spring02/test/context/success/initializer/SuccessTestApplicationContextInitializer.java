package ru.otus.group202205.homework.spring02.test.context.success.initializer;

import ru.otus.group202205.homework.spring02.test.util.BasicSystemInputApplicationContextInitializer;

public class SuccessTestApplicationContextInitializer extends BasicSystemInputApplicationContextInitializer {

  protected SuccessTestApplicationContextInitializer() {
    super("context-test/application.properties");
  }
}
