import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.group2022_05.homework.spring_01.dao.QuestionDao;
import ru.otus.group2022_05.homework.spring_01.dao.impl.QuestionDaoCsv;
import ru.otus.group2022_05.homework.spring_01.model.Question;
import ru.otus.group2022_05.homework.spring_01.service.QuestionService;
import ru.otus.group2022_05.homework.spring_01.service.impl.QuestionServiceImpl;

@DisplayName("Unit test for create question service with questions list for homework")
public class QuestionServiceTest {

  private static final List<String> EXCEPTED_QUESTION_OUTPUT = Arrays.asList(
      "Question has statement 'What is your name?' with type 'input' and answers []. Correct answer indexes list []",
      "Question has statement 'In what year was the first artificial earth satellite launched?' with type 'regular' and answers ['1957', '1961', '1977']. Correct answer indexes list [1]",
      "Question has statement 'What types of apes are anthropoids?' with type 'multiselect' and answers ['Pongo pygmaeus', 'Pongo abelii', 'Cebus capucinus']. Correct answer indexes list [1, 2]",
      "Question has statement 'In what year was Voyadger-1 launched?' with type 'regular' and answers ['1957', '1961', '1977']. Correct answer indexes list [1]",
      "Question has statement 'In what year was first manned spacecraft launched?' with type 'regular' and answers ['1957', '1961', '1977']. Correct answer indexes list [1]"
  );

  @Test
  void testQuestionServiceCreation() {
    List<String> actualQuestionOutput = getTestQuestionOutput("questions-test.csv");
    Assertions.assertEquals(EXCEPTED_QUESTION_OUTPUT, actualQuestionOutput, "Wrong actual question output!");
  }

  @Test
  void testQuestionServiceWrongOutput() {
    List<String> actualQuestionOutput = getTestQuestionOutput("questions-test-wrong.csv");
    Assertions.assertNotEquals(EXCEPTED_QUESTION_OUTPUT, actualQuestionOutput, "Wrong actual question output!");
  }

  private List<String> getTestQuestionOutput(String fileName) {
    QuestionDao questionDaoCsv = new QuestionDaoCsv(fileName);
    QuestionService questionService = new QuestionServiceImpl(questionDaoCsv);
    List<Question> questions = questionService.getQuestions();
    Assertions.assertFalse(questions.isEmpty(), "Questions list empty");
    return questions.stream().map(Question::toString).collect(Collectors.toUnmodifiableList());
  }
}
