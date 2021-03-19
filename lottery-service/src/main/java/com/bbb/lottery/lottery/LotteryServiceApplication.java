package com.bbb.lottery.lottery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LotteryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LotteryServiceApplication.class, args);
	}

}
