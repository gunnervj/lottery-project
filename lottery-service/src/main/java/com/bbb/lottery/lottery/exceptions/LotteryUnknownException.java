package com.bbb.lottery.lottery.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@NoArgsConstructor
@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class LotteryUnknownException extends  RuntimeException {

    private String errorMessage;

    public LotteryUnknownException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

}
