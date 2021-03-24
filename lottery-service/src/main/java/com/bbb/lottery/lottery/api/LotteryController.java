package com.bbb.lottery.lottery.api;

import com.bbb.lottery.lottery.api.beans.LotteryTicket;
import com.bbb.lottery.lottery.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lottery")
public class LotteryController implements LotteryAPI {
    private LotteryService lotteryService;

    @Autowired
    public LotteryController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    @Override
    @PostMapping(path = "/buy")
    public LotteryTicket buyLottery() {
        return lotteryService.buyLottery();
    }

}
