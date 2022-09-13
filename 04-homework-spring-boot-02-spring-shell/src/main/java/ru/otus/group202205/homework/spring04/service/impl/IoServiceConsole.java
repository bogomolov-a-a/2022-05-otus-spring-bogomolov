package ru.otus.group202205.homework.spring04.service.impl;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.otus.group202205.homework.spring04.service.IoService;
import ru.otus.group202205.homework.spring04.service.StreamProvider;

@Service
public class IoServiceConsole implements IoService {

  private final Scanner scanner;
  private final PrintStream printStream;

  public IoServiceConsole(StreamProvider streamProvider) {
    this.printStream = new PrintStream(streamProvider.getOutputStream(),
        true,
        StandardCharsets.UTF_8);
    scanner = new Scanner(streamProvider.getInputStream());
  }

  @Override
  public void outputMessage(String message) {
    printStream.println(message);
  }

  @Override
  public String readUserInput() {
    String result = scanner.nextLine();
    while (!StringUtils.hasText(result)) {
      result = scanner.nextLine();
    }
    return result;
  }

}
