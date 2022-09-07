package ru.otus.group202205.homework.spring03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.otus.group202205.homework.spring03.service.TestSuiteService;

@SpringBootApplication
public class StudentTestApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(StudentTestApplication.class,
        args);
    TestSuiteService questionService = context.getBean(TestSuiteService.class);
    questionService.performStudentTesting();
  }
}
