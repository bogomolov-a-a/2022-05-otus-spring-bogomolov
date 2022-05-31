package ru.otus.group202205.homework.spring02.test.context.fail.initializer;

import ru.otus.group202205.homework.spring02.test.util.BasicSystemInputApplicationContextInitializer;


public class WrongAnswerOneAttemptTestApplicationContextInitializer extends BasicSystemInputApplicationContextInitializer {

  protected WrongAnswerOneAttemptTestApplicationContextInitializer() {
    super("context-test-with-wrong-answers-one-attempt/application.properties");
  }
}
