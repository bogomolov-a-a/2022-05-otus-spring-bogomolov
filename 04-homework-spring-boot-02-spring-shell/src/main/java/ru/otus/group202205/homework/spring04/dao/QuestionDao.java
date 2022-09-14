package ru.otus.group202205.homework.spring04.dao;

import java.util.List;
import ru.otus.group202205.homework.spring04.model.Question;

public interface QuestionDao {

  List<Question> getQuestions();

}
