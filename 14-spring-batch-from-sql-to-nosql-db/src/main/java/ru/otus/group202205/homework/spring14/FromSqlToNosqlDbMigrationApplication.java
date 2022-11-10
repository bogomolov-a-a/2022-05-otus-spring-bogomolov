package ru.otus.group202205.homework.spring14;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication//(exclude = EmbeddedMongoAutoConfiguration.class)
public class FromSqlToNosqlDbMigrationApplication {

  public static void main(String[] args) {

    SpringApplication.run(FromSqlToNosqlDbMigrationApplication.class,
        args);
  }

}
