package ru.otus.group202205.homework.spring02.test.context.fail.initializer;

import ru.otus.group202205.homework.spring02.test.util.BasicSystemInputApplicationContextInitializer;

public class NoAnswerStatementTestApplicationContextInitializer extends BasicSystemInputApplicationContextInitializer {

  protected NoAnswerStatementTestApplicationContextInitializer() {
    super("context-test-no-answer-statement/application.properties");
  }
}
