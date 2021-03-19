package com.bbb.lottery.printer.api.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrintRequest {
    private String lotteryNumber;
    private String lotteryId;
}
