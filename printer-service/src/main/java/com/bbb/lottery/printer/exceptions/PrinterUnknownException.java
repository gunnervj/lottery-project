package com.bbb.lottery.printer.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@NoArgsConstructor
@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class PrinterUnknownException extends RuntimeException {
    private String errorMessage;

    public PrinterUnknownException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
