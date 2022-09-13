package ru.otus.group202205.homework.spring04.shell;

import java.util.Arrays;
import java.util.Locale;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.group202205.homework.spring04.config.properties.LocaleProperties;
import ru.otus.group202205.homework.spring04.service.TestSuiteService;

@RequiredArgsConstructor
@ShellComponent
public class TestSuiteShellComponent {

  private final TestSuiteService testSuiteService;
  private final MessageSource messageSource;
  private final LocaleProperties localeProperties;
  private boolean testingInfoRead = false;

  @ShellMethod(value = "change current testing locale by language code",
      key = {"chl", "change-locale-by-language"})
  public String changeLocaleByLanguageCode(@ShellOption String languageCode) {
    localeProperties.setLanguage(languageCode);
    Locale currentLocale = localeProperties.getCurrentLocale();
    return messageSource.getMessage("test-suite-shell-component.change-locale-command-output-message",
        new Object[]{currentLocale.toString(), languageCode},
        currentLocale);
  }

  @ShellMethod(value = "output testing conditions for user", key = {"otc", "output-testing-conditions"})
  public String outputTestingInfo() {
    testingInfoRead = true;
    return messageSource.getMessage("test-suite-shell-component.about-test-information",
        null,
        localeProperties.getCurrentLocale());
  }

  @ShellMethodAvailability("isTestingInfoRead")
  @ShellMethod(value = "execute student testing after user agreeing with testing conditions", key = {"pt", "perform-testing"})
  public String performTesting(
      @ShellOption(value = {"--student-testing-condition-state", "--stcs"}, defaultValue = "disagree") String studentTestingConditionsUserState) {
    if (!StudentTestingConditionsUserState.AGREE.equals(StudentTestingConditionsUserState.valueByCode(studentTestingConditionsUserState))) {
      return messageSource.getMessage("test-suite-shell-component.cant-to-perform-test-by-user-disagree-message",
          null,
          localeProperties.getCurrentLocale());
    }
    testSuiteService.performStudentTesting();
    return messageSource.getMessage("test-suite-shell-component.thanks-for-our-testing-application-using",
        null,
        localeProperties.getCurrentLocale());
  }

  private Availability isTestingInfoRead() {
    return testingInfoRead ? Availability.available()
        : Availability.unavailable(messageSource.getMessage("test-suite-shell-component.you-can-read-testing-conditions",
            null,
            localeProperties.getCurrentLocale()));
  }

  public enum StudentTestingConditionsUserState {
    AGREE("agree"), DISAGREE("disagree");

    static StudentTestingConditionsUserState valueByCode(String code) {
      return Arrays
          .stream(StudentTestingConditionsUserState.values())
          .filter(state -> state
              .getCode()
              .equals(code))
          .findFirst()
          .orElse(null);
    }

    @Getter
    private final String code;

    StudentTestingConditionsUserState(String code) {
      this.code = code;
    }
  }

}
