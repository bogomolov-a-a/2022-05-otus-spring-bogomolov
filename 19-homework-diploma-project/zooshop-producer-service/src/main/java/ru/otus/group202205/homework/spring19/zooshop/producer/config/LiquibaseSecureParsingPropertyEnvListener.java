package ru.otus.group202205.homework.spring19.zooshop.producer.config;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

public class LiquibaseSecureParsingPropertyEnvListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

  @Override
  public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
    event
        .getEnvironment()
        .getSystemProperties()
        .put("liquibase.secureParsing",
            "false");
  }

}
