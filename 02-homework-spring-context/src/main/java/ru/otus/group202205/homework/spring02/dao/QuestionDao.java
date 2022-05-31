package ru.otus.group202205.homework.spring02.dao;

import java.util.List;
import ru.otus.group202205.homework.spring02.model.Question;

public interface QuestionDao {

  List<Question> getQuestionsFromCsv(String csvLocation);
}
