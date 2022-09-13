package ru.otus.group202205.homework.spring04.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import javax.annotation.PreDestroy;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import ru.otus.group202205.homework.spring04.service.ServiceConfig;
import ru.otus.group202205.homework.spring04.service.StreamProvider;

@Configuration
@Import({ServiceConfig.class, MessageSourceAutoConfiguration.class})
public class IoServiceConsoleTestConfig {

  @Bean(name = "streamProvider")
  @Primary
  StreamProvider streamProvider() {
    return new IoServiceConsoleTestStreamProvider();
  }

  private static class IoServiceConsoleTestStreamProvider implements StreamProvider {

    private final OutputStream stubOutputStream = new ByteArrayOutputStream();
    private final InputStream stubInputStream;

    IoServiceConsoleTestStreamProvider() {
      InputStream stubInputStream1;
      String exceptedInputString = "first line" + System.lineSeparator() + System.lineSeparator() + "second line";
      try {
        stubInputStream1 = new ByteArrayInputStream(exceptedInputString.getBytes(StandardCharsets.UTF_8.name()));
      } catch (UnsupportedEncodingException e) {
        stubInputStream1 = null;
      }
      stubInputStream = stubInputStream1;
    }

    @Override
    public OutputStream getOutputStream() {
      return stubOutputStream;
    }

    @Override
    public InputStream getInputStream() {
      return stubInputStream;
    }

    @PreDestroy
    private void release() {
      try {
        if (stubInputStream != null) {
          stubInputStream.close();
        }
        stubOutputStream.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

  }

}
