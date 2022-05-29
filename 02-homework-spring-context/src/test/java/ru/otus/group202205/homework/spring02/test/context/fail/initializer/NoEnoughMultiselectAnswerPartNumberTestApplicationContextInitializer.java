package ru.otus.group202205.homework.spring02.test.context.fail.initializer;

import ru.otus.group202205.homework.spring02.test.util.BasicSystemInputApplicationContextInitializer;

public class NoEnoughMultiselectAnswerPartNumberTestApplicationContextInitializer extends BasicSystemInputApplicationContextInitializer {

  protected NoEnoughMultiselectAnswerPartNumberTestApplicationContextInitializer() {
    super("context-test-no-enough-mutiselect-answer-part-number/application.properties");
  }
}
