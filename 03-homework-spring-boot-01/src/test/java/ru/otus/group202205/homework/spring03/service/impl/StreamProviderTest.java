package ru.otus.group202205.homework.spring03.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.group202205.homework.spring03.config.StreamProviderTestConfig;
import ru.otus.group202205.homework.spring03.service.AnswerConverter;
import ru.otus.group202205.homework.spring03.service.AnswerService;
import ru.otus.group202205.homework.spring03.service.IoService;
import ru.otus.group202205.homework.spring03.service.QuestionConverter;
import ru.otus.group202205.homework.spring03.service.QuestionService;
import ru.otus.group202205.homework.spring03.service.StreamProvider;
import ru.otus.group202205.homework.spring03.service.TestSuiteService;

@SpringBootTest(classes = {StreamProviderTestConfig.class})
class StreamProviderTest {

  @MockBean
  private AnswerService answerService;
  @MockBean
  private AnswerConverter answerConverter;
  @MockBean
  private QuestionService questionService;
  @MockBean
  private IoService ioService;
  @MockBean
  private TestSuiteService testSuiteService;
  @MockBean
  private QuestionConverter questionConverter;
  @Autowired
  private StreamProvider streamProvider;

  @Test
  void shouldBeReturnSystemOutAsOutputStream() {
    assertThat(streamProvider.getOutputStream()).isEqualTo(System.out);
  }

  @Test
  void shouldBeReturnSystemInAsInputStream() {
    assertThat(streamProvider.getInputStream()).isEqualTo(System.in);
  }
}
