package ru.otus.group202205.homework.spring19.zooshop.order.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(title = "Error information")
public class ErrorInfo {

  @Schema(title = "Target url", description = "The URL that encountered an error while processing ")
  public final String url;
  @Schema(title = "Error cause")
  public final String message;

  public ErrorInfo(String url, RuntimeException ex) {
    this.url = url;
    this.message = ex.getMessage();
  }

}
