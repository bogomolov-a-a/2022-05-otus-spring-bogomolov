package ru.otus.group2022_05.homework.spring_01.service.impl;

import java.util.List;
import ru.otus.group2022_05.homework.spring_01.model.Question;
import ru.otus.group2022_05.homework.spring_01.service.QuestionService;
import ru.otus.group2022_05.homework.spring_01.dao.QuestionDao;

public class QuestionServiceImpl implements QuestionService {

  private final QuestionDao questionDao;

  public QuestionServiceImpl(QuestionDao questionDao) {
    this.questionDao = questionDao;
  }


  @Override
  public List<Question> getQuestions() {
    return questionDao.getQuestions();
  }
}
