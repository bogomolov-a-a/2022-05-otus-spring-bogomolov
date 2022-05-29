package ru.otus.group202205.homework.spring02.test.context.fail.initializer;

import ru.otus.group202205.homework.spring02.test.util.BasicSystemInputApplicationContextInitializer;


public class NoQuestionStatementTestApplicationContextInitializer extends BasicSystemInputApplicationContextInitializer {

  protected NoQuestionStatementTestApplicationContextInitializer() {
    super("context-test-no-question-statement/application.properties");
  }
}
