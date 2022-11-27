package ru.otus.group202205.homework.spring19.zooshop.order.exception;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderExceptionHandler {

  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(RuntimeException.class)
  @ResponseBody
  public ErrorInfo handleRuntimeException(HttpServletRequest req, RuntimeException ex) {
    return new ErrorInfo(req
        .getRequestURL()
        .toString(),
        ex);
  }

}
