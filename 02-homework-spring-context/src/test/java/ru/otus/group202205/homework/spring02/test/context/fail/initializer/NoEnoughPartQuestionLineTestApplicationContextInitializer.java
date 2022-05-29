package ru.otus.group202205.homework.spring02.test.context.fail.initializer;

import ru.otus.group202205.homework.spring02.test.util.BasicSystemInputApplicationContextInitializer;

public class NoEnoughPartQuestionLineTestApplicationContextInitializer extends BasicSystemInputApplicationContextInitializer {

  protected NoEnoughPartQuestionLineTestApplicationContextInitializer() {
    super("context-test-no-enough-parts/application.properties");
  }
}
