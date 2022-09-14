package ru.otus.group202205.homework.spring04.config.properties;

import java.util.Locale;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("app.locale")
public class LocaleProperties {

  private String language;

  public Locale getCurrentLocale() {
    return Locale.forLanguageTag(language);
  }

}
