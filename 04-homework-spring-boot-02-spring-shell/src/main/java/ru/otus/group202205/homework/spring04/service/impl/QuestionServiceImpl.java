package ru.otus.group202205.homework.spring04.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring04.dao.QuestionDao;
import ru.otus.group202205.homework.spring04.model.Question;
import ru.otus.group202205.homework.spring04.service.QuestionService;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

  private final QuestionDao questionDao;

  @Override
  public List<Question> getQuestions() {
    return questionDao.getQuestions();
  }

}
