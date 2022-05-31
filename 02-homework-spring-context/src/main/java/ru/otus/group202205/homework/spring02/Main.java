package ru.otus.group202205.homework.spring02;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.group202205.homework.spring02.config.AppConfig;
import ru.otus.group202205.homework.spring02.service.StudentTestService;

public class Main {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext(AppConfig.class);
    runApplicationContext(context);
    context.close();
  }

  public static void runApplicationContext(ApplicationContext context) {
    StudentTestService questionService = context.getBean(StudentTestService.class);
    questionService.performStudentTesting();
  }

}
