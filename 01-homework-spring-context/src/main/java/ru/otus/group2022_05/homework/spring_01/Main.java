package ru.otus.group2022_05.homework.spring_01;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.group2022_05.homework.spring_01.service.QuestionService;

public class Main {

  public static void main(String[] args) {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
    QuestionService questionService = context.getBean(QuestionService.class);
    System.out.println("Student test has following question list:");
    questionService.getQuestions().forEach(question -> System.out.println(question.toString()));
    context.close();
  }
}
