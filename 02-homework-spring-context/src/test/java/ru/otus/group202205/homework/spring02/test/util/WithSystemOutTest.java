package ru.otus.group202205.homework.spring02.test.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import ru.otus.group202205.homework.spring02.Main;

@RequiredArgsConstructor
public abstract class WithSystemOutTest extends BaseContextTest {

  private final Boolean allowError;
  @Setter
  private Class<? extends Exception> exceptionClass;
  private PrintStream sout;
  private ByteArrayOutputStream tempOut;

  protected void runContext(ApplicationContext context) {
    Main.runApplicationContext(context);
  }

  protected void handleOutput(ByteArrayOutputStream tempOut, ClassPathResource appOutputResource) {
    String exceptedAppOutput;
    try (InputStream appOutputStream = appOutputResource.getInputStream()) {
      exceptedAppOutput = new String(StreamUtils.copyToByteArray(appOutputStream), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    String actualAppOutput = tempOut.toString();
    Assertions.assertEquals(exceptedAppOutput, actualAppOutput, "Wrong app output,may be you change code and don't correct tests.");
    System.out.println("main method executing test passed!");
  }

  @BeforeEach
  void initializeTests() {
    sout = System.out;
    tempOut = new ByteArrayOutputStream();
    System.setOut(new PrintStream(tempOut));
  }

  @DisplayName("Executing 'main' method with lifted context for specified configuration and context initializer")
  @Test
  void emulateMainTest(@Value("${test.app-output.filename}") ClassPathResource appOutputResource) {
    if (allowError) {
      Assertions.assertThrows(exceptionClass, () -> runContext(context));
      handleOutput(tempOut, appOutputResource);
      return;
    }
    runContext(context);
    handleOutput(tempOut, appOutputResource);
  }

  @AfterEach
  void finalizeTests() {
    try {
      System.in.close();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    System.out.close();
    System.setIn(null);
    System.setOut(sout);
    tempOut = null;
  }
}
