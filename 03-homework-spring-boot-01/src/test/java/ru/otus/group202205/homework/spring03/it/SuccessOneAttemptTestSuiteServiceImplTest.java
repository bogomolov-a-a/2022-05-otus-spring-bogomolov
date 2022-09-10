package ru.otus.group202205.homework.spring03.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.group202205.homework.spring03.config.TestSuiteServiceImplTestConfig;
import ru.otus.group202205.homework.spring03.config.properties.IoStreamTestProperties;
import ru.otus.group202205.homework.spring03.service.StreamProvider;
import ru.otus.group202205.homework.spring03.service.TestSuiteService;

@SpringBootTest(classes = TestSuiteServiceImplTestConfig.class)
@ActiveProfiles("success-one-attempt")
class SuccessOneAttemptTestSuiteServiceImplTest {

  @Autowired
  private IoStreamTestProperties ioStreamTestProperties;
  @Autowired
  private TestSuiteService testSuiteService;
  @Autowired
  private StreamProvider streamProvider;

  @Test
  void shouldBeStudentTestingPassedOnOneAttempt() {
    testSuiteService.performStudentTesting();
    OutputStream outputStream = streamProvider.getOutputStream();
    assertThat(outputStream).isInstanceOf(ByteArrayOutputStream.class);
    String actualStudentTestSuiteOutput = ((ByteArrayOutputStream) outputStream)
        .toString(StandardCharsets.UTF_8)
        .replace(System.lineSeparator(),
            "\n");
    URL exceptedSystemOutUri = SuccessOneAttemptTestSuiteServiceImplTest.class
        .getClassLoader()
        .getResource(ioStreamTestProperties.getSystemOutputStubResourceName());
    byte[] exceptedSystemOutBytes;
    try {
      exceptedSystemOutBytes = Files.readAllBytes(Paths.get(Objects
          .requireNonNull(exceptedSystemOutUri)
          .toURI()));
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
    String exceptedStudentTestSuiteOutput = new String(exceptedSystemOutBytes,
        StandardCharsets.UTF_8);
    assertThat(actualStudentTestSuiteOutput).isEqualTo(exceptedStudentTestSuiteOutput);
  }
}