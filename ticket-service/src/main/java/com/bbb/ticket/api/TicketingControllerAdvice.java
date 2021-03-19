package com.bbb.ticket.api;

import com.bbb.ticket.api.bean.Error;
import com.bbb.ticket.exceptions.TicketingUnknownException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TicketingControllerAdvice {

    @ExceptionHandler(value = TicketingUnknownException.class)
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    public @ResponseBody
    Error handleUnknownException(final TicketingUnknownException exception) {
        return Error.builder()
                    .message(exception.getMessage())
                    .build();
    }

}
