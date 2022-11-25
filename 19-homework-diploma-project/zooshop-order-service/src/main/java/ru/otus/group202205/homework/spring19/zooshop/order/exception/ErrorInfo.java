package ru.otus.group202205.homework.spring19.zooshop.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorInfo {

  public final String url;
  public final String ex;

  public ErrorInfo(String url, RuntimeException ex) {
    this.url = url;
    this.ex = ex.getMessage();
  }

}