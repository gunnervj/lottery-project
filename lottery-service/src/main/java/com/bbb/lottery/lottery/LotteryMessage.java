package com.bbb.lottery.lottery;

public enum LotteryMessage {
    UNKNOWN_ERROR("Something went. Please try again or try again later if issue persist.");

    private final String message;

    LotteryMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
