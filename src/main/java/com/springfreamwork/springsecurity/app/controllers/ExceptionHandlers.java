package com.springfreamwork.springsecurity.app.controllers;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(Exception.class)
    public String exceptionHandling(Exception ex) {
        return ex.getMessage();
    }
}
