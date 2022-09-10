package ru.otus.group202205.homework.spring03.service;

import java.util.List;
import ru.otus.group202205.homework.spring03.model.Answer;

public interface AnswerConverter {

  String convertToString(List<Answer> answers);
}
