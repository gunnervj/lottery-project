package com.bbb.ticket.api.bean;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Error {
    private String message;
}
