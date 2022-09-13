package ru.otus.group202205.homework.spring04.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.shell.Shell;
import org.springframework.shell.boot.CommandCatalogAutoConfiguration;
import org.springframework.shell.boot.CompleterAutoConfiguration;
import org.springframework.shell.boot.ComponentFlowAutoConfiguration;
import org.springframework.shell.boot.ExitCodeAutoConfiguration;
import org.springframework.shell.boot.JLineShellAutoConfiguration;
import org.springframework.shell.boot.ParameterResolverAutoConfiguration;
import org.springframework.shell.boot.ShellContextAutoConfiguration;
import org.springframework.shell.boot.SpringShellAutoConfiguration;
import org.springframework.shell.boot.StandardAPIAutoConfiguration;
import org.springframework.shell.result.ResultHandlerConfig;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.group202205.homework.spring04.config.TestSuiteServiceImplTestConfig;
import ru.otus.group202205.homework.spring04.config.properties.IoStreamTestProperties;
import ru.otus.group202205.homework.spring04.service.StreamProvider;
import ru.otus.group202205.homework.spring04.shell.TestSuiteShellComponent;
import ru.otus.group202205.homework.spring04.shell.TestSuiteShellComponent.StudentTestingConditionsUserState;

@SpringBootTest(
    classes = {TestSuiteServiceImplTestConfig.class, SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class, ResultHandlerConfig.class,
        CommandCatalogAutoConfiguration.class, ShellContextAutoConfiguration.class, ExitCodeAutoConfiguration.class, CompleterAutoConfiguration.class,
        ParameterResolverAutoConfiguration.class, ComponentFlowAutoConfiguration.class, StandardAPIAutoConfiguration.class, TestSuiteShellComponent.class})
@ActiveProfiles("success-one-attempt")
class SuccessOneAttemptTestSuiteShellComponentTest {

  private static final String CHANGE_LOCALE_COMMAND = "chl";
  private static final Locale LOCALE_RU = Locale.forLanguageTag("ru");
  private static final String OUTPUT_TEST_CONDITION_COMMAND = "otc";
  private static final String PERFORM_TESTING_LONG_COMMAND = "perform-testing";
  private static final String PERFORM_TESTING_COMMAND_STUDENT_TESTING_STATE_LONG_PARAM = "--student-testing-condition-state";
  @Autowired
  private Shell shell;
  @Autowired
  private StreamProvider streamProvider;
  @Autowired
  private IoStreamTestProperties ioStreamTestProperties;
  @Autowired
  private MessageSource messageSource;

  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  @Test
  void shouldBeStudentTestingPassedOnOneAttempt() {
    assertThat(shell.evaluate(() -> String.join(" ",
        CHANGE_LOCALE_COMMAND,
        LOCALE_RU.getLanguage())))
        .isNotNull()
        .isInstanceOf(String.class);
    assertThat(shell.evaluate(() -> OUTPUT_TEST_CONDITION_COMMAND))
        .isNotNull()
        .isInstanceOf(String.class);
    String output = (String) shell.evaluate(() -> String.join(" ",
        PERFORM_TESTING_LONG_COMMAND,
        PERFORM_TESTING_COMMAND_STUDENT_TESTING_STATE_LONG_PARAM,
        StudentTestingConditionsUserState.AGREE.getCode()));
    OutputStream outputStream = streamProvider.getOutputStream();
    assertThat(outputStream).isInstanceOf(ByteArrayOutputStream.class);
    String actualStudentTestSuiteOutput = ((ByteArrayOutputStream) outputStream)
        .toString(StandardCharsets.UTF_8)
        .replace(System.lineSeparator(),
            "\n");
    URL exceptedSystemOutUri = SuccessOneAttemptTestSuiteServiceImplTest.class
        .getClassLoader()
        .getResource(ioStreamTestProperties.getSystemOutputStubResourceName());
    byte[] exceptedSystemOutBytes;
    try {
      exceptedSystemOutBytes = Files.readAllBytes(Paths.get(Objects
          .requireNonNull(exceptedSystemOutUri)
          .toURI()));
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
    String exceptedStudentTestSuiteOutput = new String(exceptedSystemOutBytes,
        StandardCharsets.UTF_8);
    assertThat(actualStudentTestSuiteOutput).isEqualTo(exceptedStudentTestSuiteOutput);
    assertThat(output).isEqualTo(messageSource.getMessage("test-suite-shell-component.thanks-for-our-testing-application-using",
        null,
        LOCALE_RU));
  }

}