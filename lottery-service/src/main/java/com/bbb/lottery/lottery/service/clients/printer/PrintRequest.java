package com.bbb.lottery.lottery.service.clients.printer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class PrintRequest {
    private String lotteryNumber;
    private String lotteryId;
}
