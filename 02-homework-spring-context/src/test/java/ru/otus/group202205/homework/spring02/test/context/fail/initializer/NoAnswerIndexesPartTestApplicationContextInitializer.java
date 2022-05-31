package ru.otus.group202205.homework.spring02.test.context.fail.initializer;

import ru.otus.group202205.homework.spring02.test.util.BasicSystemInputApplicationContextInitializer;

public class NoAnswerIndexesPartTestApplicationContextInitializer extends BasicSystemInputApplicationContextInitializer {

  protected NoAnswerIndexesPartTestApplicationContextInitializer() {
    super("context-test-no-answer-indexes-part/application.properties");
  }
}
