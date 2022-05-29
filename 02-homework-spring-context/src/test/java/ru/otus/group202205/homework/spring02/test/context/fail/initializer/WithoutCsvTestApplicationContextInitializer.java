package ru.otus.group202205.homework.spring02.test.context.fail.initializer;

import ru.otus.group202205.homework.spring02.test.util.BasicSystemInputApplicationContextInitializer;


public class WithoutCsvTestApplicationContextInitializer extends BasicSystemInputApplicationContextInitializer {

  protected WithoutCsvTestApplicationContextInitializer() {
    super("context-test-without-csv/application.properties");
  }
}
