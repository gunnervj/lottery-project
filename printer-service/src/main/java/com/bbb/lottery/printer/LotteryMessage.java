package com.bbb.lottery.printer;

public enum LotteryMessage {
    UNKNOWN_ERROR("Oops something went wrong. Try later");

    private final String message;

    LotteryMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
