package ru.otus.group202205.homework.spring03.service.impl;

import java.io.InputStream;
import java.io.OutputStream;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring03.service.StreamProvider;

@Service
public class StreamProviderConsole implements StreamProvider {

  @Override
  public OutputStream getOutputStream() {
    return System.out;
  }

  @Override
  public InputStream getInputStream() {
    return System.in;
  }
}
