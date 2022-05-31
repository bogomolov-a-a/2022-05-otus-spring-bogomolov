package ru.otus.group202205.homework.spring02.test.context.fail.initializer;

import ru.otus.group202205.homework.spring02.test.util.BasicSystemInputApplicationContextInitializer;


public class WrongAnswerIndexesPartTestApplicationContextInitializer extends BasicSystemInputApplicationContextInitializer {

  protected WrongAnswerIndexesPartTestApplicationContextInitializer() {
    super("context-test-with-wrong-answer-indexes-part/application.properties");
  }
}
