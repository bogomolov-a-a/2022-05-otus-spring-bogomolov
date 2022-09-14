package ru.otus.group202205.homework.spring04.shell;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.shell.Availability;
import org.springframework.shell.CommandNotCurrentlyAvailable;
import org.springframework.shell.Shell;
import org.springframework.shell.command.CommandExecution.CommandParserExceptionsException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import ru.otus.group202205.homework.spring04.StudentTestApplication;
import ru.otus.group202205.homework.spring04.shell.TestSuiteShellComponent.StudentTestingConditionsUserState;

@SpringBootTest(classes = StudentTestApplication.class)
@DirtiesContext
class TestSuiteShellComponentTest {

  private static final String CHANGE_LOCALE_COMMAND = "chl";
  private static final String CHANGE_LOCALE_LONG_COMMAND = "change-locale-by-language";
  private static final Locale LOCALE_EN = Locale.ENGLISH;
  private static final Locale LOCALE_RU = Locale.forLanguageTag("ru");
  private static final String OUTPUT_TEST_CONDITION_COMMAND = "otc";
  private static final String OUTPUT_TEST_CONDITION_LONG_COMMAND = "output-testing-conditions";
  private static final String PERFORM_TESTING_COMMAND = "pt";
  private static final String PERFORM_TESTING_LONG_COMMAND = "perform-testing";
  private static final String PERFORM_TESTING_COMMAND_STUDENT_TESTING_STATE_PARAM = "--stcs";
  private static final String PERFORM_TESTING_COMMAND_STUDENT_TESTING_STATE_LONG_PARAM = "--student-testing-condition-state";
  @Autowired
  private Shell shell;
  @Autowired
  private MessageSource messageSource;

  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  @Test
  void shouldBeLocaleNotChangedBecauseParameterIsEmpty() {
    beLocaleNotChangedBecauseParameterIsEmpty(CHANGE_LOCALE_COMMAND);
    beLocaleNotChangedBecauseParameterIsEmpty(CHANGE_LOCALE_LONG_COMMAND);
  }

  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  @Test
  void shouldBeLocaleChanged() {
    beLocaleChanged(LOCALE_EN);
    beLocaleChanged(LOCALE_RU);
  }

  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  @Test
  void shouldBeOutputTestC999999999999999onditions() {
    assertThat(shell.evaluate(() -> String.join(" ",
        CHANGE_LOCALE_COMMAND,
        LOCALE_EN.getLanguage())))
        .isNotNull()
        .isInstanceOf(String.class);
    assertThat(shell.evaluate(() -> OUTPUT_TEST_CONDITION_COMMAND))
        .isNotNull()
        .isEqualTo(messageSource.getMessage("test-suite-shell-component.about-test-information",
            null,
            LOCALE_EN));
    assertThat(shell.evaluate(() -> OUTPUT_TEST_CONDITION_LONG_COMMAND))
        .isNotNull()
        .isEqualTo(messageSource.getMessage("test-suite-shell-component.about-test-information",
            null,
            LOCALE_EN));
  }

  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  @Test
  void shouldBeFailTestingExecutingCauseNeedReadTestingConditions() {
    beFailedTestingExecutingCauseNeedReadTestingConditions(PERFORM_TESTING_COMMAND);
    beFailedTestingExecutingCauseNeedReadTestingConditions(PERFORM_TESTING_LONG_COMMAND);
  }

  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  @Test
  void shouldBeUserDisagreeWithTestingConditionsBecauseParameterDefaultValueDisagree() {
    beUserDisagreeWithTestingConditionsBecauseParameterDefaultValueDisagree(PERFORM_TESTING_COMMAND);
    beUserDisagreeWithTestingConditionsBecauseParameterDefaultValueDisagree(PERFORM_TESTING_LONG_COMMAND);
  }

  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  @Test
  void shouldBeUserDisagreeWithTestingConditionsBecauseParameterDisagree() {
    beUserDisagreeWithTestingConditionsBecauseParameterDisagree(PERFORM_TESTING_COMMAND);
    beUserDisagreeWithTestingConditionsBecauseParameterDisagree(PERFORM_TESTING_LONG_COMMAND);
  }

  private void beFailedTestingExecutingCauseNeedReadTestingConditions(String command) {
    assertThat(shell.evaluate(() -> String.join(" ",
        CHANGE_LOCALE_COMMAND,
        LOCALE_EN.getLanguage())))
        .isNotNull()
        .isInstanceOf(String.class);
    Object commandNotAvailableObject = shell.evaluate(() -> command);
    assertThat(commandNotAvailableObject)
        .isNotNull()
        .isInstanceOf(CommandNotCurrentlyAvailable.class);
    CommandNotCurrentlyAvailable commandNotCurrentlyAvailable = (CommandNotCurrentlyAvailable) commandNotAvailableObject;
    Availability available = commandNotCurrentlyAvailable.getAvailability();
    assertThat(available).isNotNull();
    assertThat(available.getReason())
        .isNotNull()
        .isEqualTo(messageSource.getMessage("test-suite-shell-component.you-can-read-testing-conditions",
            null,
            LOCALE_EN));
  }

  private void beUserDisagreeWithTestingConditionsBecauseParameterDefaultValueDisagree(String command) {
    assertThat(shell.evaluate(() -> String.join(" ",
        CHANGE_LOCALE_COMMAND,
        LOCALE_EN.getLanguage())))
        .isNotNull()
        .isInstanceOf(String.class);
    assertThat(shell.evaluate(() -> String.join(" ",
        OUTPUT_TEST_CONDITION_COMMAND,
        LOCALE_EN.getLanguage())))
        .isNotNull()
        .isInstanceOf(String.class);
    assertThat(shell.evaluate(() -> command))
        .isNotNull()
        .isEqualTo(messageSource.getMessage("test-suite-shell-component.cant-to-perform-test-by-user-disagree-message",
            null,
            LOCALE_EN));
  }

  private void beUserDisagreeWithTestingConditionsBecauseParameterDisagree(String command) {
    assertThat(shell.evaluate(() -> String.join(" ",
        CHANGE_LOCALE_COMMAND,
        LOCALE_EN.getLanguage())))
        .isNotNull()
        .isInstanceOf(String.class);
    assertThat(shell.evaluate(() -> OUTPUT_TEST_CONDITION_COMMAND))
        .isNotNull()
        .isInstanceOf(String.class);
    assertThat(shell.evaluate(() -> String.join(" ",
        command,
        PERFORM_TESTING_COMMAND_STUDENT_TESTING_STATE_LONG_PARAM,
        StudentTestingConditionsUserState.DISAGREE.getCode())))
        .isNotNull()
        .isEqualTo(messageSource.getMessage("test-suite-shell-component.cant-to-perform-test-by-user-disagree-message",
            null,
            LOCALE_EN));
    assertThat(shell.evaluate(() -> String.join(" ",
        command,
        PERFORM_TESTING_COMMAND_STUDENT_TESTING_STATE_PARAM,
        StudentTestingConditionsUserState.DISAGREE.getCode())))
        .isNotNull()
        .isEqualTo(messageSource.getMessage("test-suite-shell-component.cant-to-perform-test-by-user-disagree-message",
            null,
            LOCALE_EN));
  }

  private void beLocaleNotChangedBecauseParameterIsEmpty(String command) {
    assertThat(shell.evaluate(() -> command))
        .isNotNull()
        .isInstanceOf(CommandParserExceptionsException.class);
  }

  private void beLocaleChanged(Locale locale) {
    String languageCode = locale.getLanguage();
    assertThat(shell.evaluate(() -> String.join(" ",
        CHANGE_LOCALE_COMMAND,
        languageCode)))
        .isNotNull()
        .isEqualTo(messageSource.getMessage("test-suite-shell-component.change-locale-command-output-message",
            new Object[]{languageCode, languageCode},
            locale));
  }

}