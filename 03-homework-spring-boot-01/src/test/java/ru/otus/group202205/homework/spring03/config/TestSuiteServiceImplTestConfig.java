package ru.otus.group202205.homework.spring03.config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.PreDestroy;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import ru.otus.group202205.homework.spring03.config.properties.IoStreamPropertiesConfiguration;
import ru.otus.group202205.homework.spring03.config.properties.IoStreamTestProperties;
import ru.otus.group202205.homework.spring03.config.properties.PropertiesConfiguration;
import ru.otus.group202205.homework.spring03.dao.DaoConfig;
import ru.otus.group202205.homework.spring03.service.ServiceConfig;
import ru.otus.group202205.homework.spring03.service.StreamProvider;

@Import({DaoConfig.class, ServiceConfig.class, PropertiesConfiguration.class, MessageSourceAutoConfiguration.class, IoStreamPropertiesConfiguration.class})
public class TestSuiteServiceImplTestConfig {

  @Bean(name = "streamProvider")
  @Primary
  public StreamProvider streamProvider(IoStreamTestProperties ioStreamTestProperties) {
    return new StreamProviderResourceInputImpl(ioStreamTestProperties);
  }

  private static class StreamProviderResourceInputImpl implements StreamProvider {

    private InputStream resourceInputStream;
    private OutputStream outputStream;

    public StreamProviderResourceInputImpl(IoStreamTestProperties ioStreamTestProperties) {
      resourceInputStream = StreamProviderResourceInputImpl.class
          .getClassLoader()
          .getResourceAsStream(ioStreamTestProperties.getSystemInputStubResourceName());
      outputStream = new ByteArrayOutputStream();
    }

    @Override
    public OutputStream getOutputStream() {
      return outputStream;
    }

    @Override
    public InputStream getInputStream() {
      return resourceInputStream;
    }

    @PreDestroy
    private void release() {
      try {
        if (resourceInputStream != null) {
          resourceInputStream.close();
          resourceInputStream = null;
        }
        if (outputStream != null) {
          outputStream.close();
          outputStream = null;
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

  }
}
