package ru.otus.group202205.homework.spring07.config;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

/*
 * https://forum.liquibase.org/t/liquibase-secureparsing-user-questions/6973
 * For start without explicit parameter set, we be added it in application listener.
 * */
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
