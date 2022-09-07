package ru.otus.group202205.homework.spring03.service;

import ru.otus.group202205.homework.spring03.model.Question;

public interface QuestionConverter {

  String convertToString(Question question, int questionIndex);
}
