package ru.otus.group202205.homework.spring02.test.context.fail.initializer;

import ru.otus.group202205.homework.spring02.test.util.BasicSystemInputApplicationContextInitializer;


public class NoQuestionTypeTestApplicationContextInitializer extends BasicSystemInputApplicationContextInitializer {

  protected NoQuestionTypeTestApplicationContextInitializer() {
    super("context-test-no-question-type/application.properties");
  }
}
