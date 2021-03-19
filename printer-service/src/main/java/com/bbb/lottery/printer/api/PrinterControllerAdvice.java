package com.bbb.lottery.printer.api;

import com.bbb.lottery.printer.api.beans.Error;
import com.bbb.lottery.printer.exceptions.PrinterUnknownException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PrinterControllerAdvice {

    @ExceptionHandler(value = PrinterUnknownException.class)
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    public @ResponseBody
    Error handleUnknownException(final PrinterUnknownException exception) {
        return Error.builder()
                    .message(exception.getMessage())
                    .build();
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    Error handleIllegalArgumentException(final IllegalArgumentException exception) {
        return Error.builder()
                .message(exception.getMessage())
                .build();
    }

}
