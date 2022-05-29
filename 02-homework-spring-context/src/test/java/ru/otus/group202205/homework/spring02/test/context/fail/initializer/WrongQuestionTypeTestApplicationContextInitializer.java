package ru.otus.group202205.homework.spring02.test.context.fail.initializer;

import ru.otus.group202205.homework.spring02.test.util.BasicSystemInputApplicationContextInitializer;

public class WrongQuestionTypeTestApplicationContextInitializer extends BasicSystemInputApplicationContextInitializer {

  protected WrongQuestionTypeTestApplicationContextInitializer() {
    super("context-test-with-wrong-question-type/application.properties");
  }
}
