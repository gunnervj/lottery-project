package com.bbb.lottery.printer.api.beans;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Error {
    private String message;
}
