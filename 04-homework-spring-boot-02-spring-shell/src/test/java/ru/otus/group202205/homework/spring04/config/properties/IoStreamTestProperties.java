package ru.otus.group202205.homework.spring04.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("app.it")
public class IoStreamTestProperties {

  private String systemInputStubResourceName;
  private String systemOutputStubResourceName;

}
