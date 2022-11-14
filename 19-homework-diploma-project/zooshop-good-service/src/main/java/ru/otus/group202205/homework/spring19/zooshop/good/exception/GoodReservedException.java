package ru.otus.group202205.homework.spring19.zooshop.good.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class GoodReservedException extends RuntimeException {

}
