package ru.otus.group202205.homework.spring03.service;

import java.io.InputStream;
import java.io.OutputStream;

public interface StreamProvider {

  OutputStream getOutputStream();

  InputStream getInputStream();
}
