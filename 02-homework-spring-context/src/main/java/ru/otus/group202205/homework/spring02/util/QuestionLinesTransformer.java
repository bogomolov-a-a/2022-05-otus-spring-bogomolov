package ru.otus.group202205.homework.spring02.util;

import java.util.List;
import ru.otus.group202205.homework.spring02.model.Question;

public interface QuestionLinesTransformer {

  List<Question> transformQuestionLinesToQuestion(List<String> questionLines);
}
