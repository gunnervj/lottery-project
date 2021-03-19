package com.bbb.lottery.printer.api.beans;

import lombok.Builder;
import lombok.Getter;

import java.net.URL;

@Getter
@Builder
public class PrintResponse {
    private URL location;
    private String lotteryId;
}
