package ru.otus.group202205.homework.spring02.util.impl;

import java.util.Scanner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ConsoleUtil {

  private final Scanner scanner = new Scanner(System.in);

  public void printMessageToConsole(String message) {
    System.out.print(message + "\n");
  }

  public String readNextLineFromConsole() {
    String result = scanner.nextLine();
    while (!StringUtils.hasText(result)) {
      result = scanner.nextLine();
    }
    return result;
  }

}
