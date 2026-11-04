package com.example.online_store.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(
            RuntimeException exception,
            Model model
    ) {

        model.addAttribute(
                "message",
                exception.getMessage()
        );

        return "error";
    }
}
