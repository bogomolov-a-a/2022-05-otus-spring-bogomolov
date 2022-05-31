package ru.otus.group202205.homework.spring02.util;

import java.util.List;

public interface QuestionLinesReader {

  List<String> readQuestionLineFromCsv(String csvLocation);
}
