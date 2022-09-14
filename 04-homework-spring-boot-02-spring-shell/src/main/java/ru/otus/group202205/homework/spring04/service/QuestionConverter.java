package ru.otus.group202205.homework.spring04.service;

import ru.otus.group202205.homework.spring04.model.Question;

public interface QuestionConverter {

  String convertToString(Question question, int questionIndex);

}
