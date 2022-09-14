package ru.otus.group202205.homework.spring04.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.group202205.homework.spring04.config.IoServiceConsoleTestConfig;
import ru.otus.group202205.homework.spring04.service.AnswerConverter;
import ru.otus.group202205.homework.spring04.service.AnswerService;
import ru.otus.group202205.homework.spring04.service.IoService;
import ru.otus.group202205.homework.spring04.service.QuestionConverter;
import ru.otus.group202205.homework.spring04.service.QuestionService;
import ru.otus.group202205.homework.spring04.service.StreamProvider;
import ru.otus.group202205.homework.spring04.service.TestSuiteService;

@SpringBootTest(classes = {IoServiceConsoleTestConfig.class})
@DirtiesContext
class IoServiceConsoleTest {

  @MockBean
  private AnswerConverter answerConverter;
  @MockBean
  private QuestionConverter questionConverter;
  @MockBean
  private QuestionService questionService;
  @MockBean
  private AnswerService answerService;
  @MockBean
  private TestSuiteService testSuiteService;
  @Autowired
  private IoService ioService;
  @Autowired
  private StreamProvider streamProvider;

  @Test
  void shouldBeReadLineFromStreamEmptyStringExists() {
    String firstLine = ioService.readUserInput();
    String secondLine = ioService.readUserInput();
    assertThat(firstLine).isEqualTo("first line");
    assertThat(secondLine).isEqualTo("second line");
    assertThatThrownBy(ioService::readUserInput)
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("No line found");
  }

  @Test
  void shouldBeWriteLineToStream() {
    String exceptedOutputString = "Test message for output in output stream";
    ioService.outputMessage(exceptedOutputString);
    String actualOutputString;
    try (OutputStream outputStream = streamProvider.getOutputStream()) {
      assertThat(outputStream).isInstanceOf(ByteArrayOutputStream.class);
      actualOutputString = ((ByteArrayOutputStream) outputStream).toString(StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertThat(actualOutputString).isEqualTo(exceptedOutputString + System.lineSeparator());
  }

}
