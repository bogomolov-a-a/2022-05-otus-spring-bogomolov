package ru.otus.group202205.homework.spring03.dao;

import java.util.List;
import ru.otus.group202205.homework.spring03.model.Question;

public interface QuestionLinesTransformer {

  List<Question> transformQuestionInputToQuestion(List<String> questionLines);
}
