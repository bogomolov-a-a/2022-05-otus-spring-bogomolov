package ru.otus.group202205.homework.spring09.exception;

public class LibraryGeneralException extends RuntimeException {

  public LibraryGeneralException(String message, Throwable cause) {
    super(message,
        cause);
  }

  @Override
  public String toString() {
    return String.format("%s Cause %s",
        getMessage(),
        getCause().getMessage());
  }

}
