package ru.otus.group202205.homework.spring02.test.context.fail.initializer;

import ru.otus.group202205.homework.spring02.test.util.BasicSystemInputApplicationContextInitializer;


public class NoQuestionTestApplicationContextInitializer extends BasicSystemInputApplicationContextInitializer {

  protected NoQuestionTestApplicationContextInitializer() {
    super("context-test-no-question/application.properties");
  }
}
