package com.bbb.lottery.lottery.api;

import com.bbb.lottery.lottery.api.beans.Error;
import com.bbb.lottery.lottery.exceptions.LotteryUnknownException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class LotteryControllerAdvice {

    @ExceptionHandler(value = LotteryUnknownException.class)
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    public @ResponseBody
    Error handleUnknownException(final LotteryUnknownException exception) {
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
