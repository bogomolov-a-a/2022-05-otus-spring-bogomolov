package ru.otus.group202205.homework.spring02.test.context.fail.initializer;

import ru.otus.group202205.homework.spring02.test.util.BasicSystemInputApplicationContextInitializer;

public class WrongTypingTwoAttemptTestApplicationContextInitializer extends BasicSystemInputApplicationContextInitializer {

  protected WrongTypingTwoAttemptTestApplicationContextInitializer() {
    super("context-test-with-wrong-typing-two-attempt/application.properties");
  }
}
