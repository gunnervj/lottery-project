package com.bbb.ticket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class BeanConfigs {

    @Bean
    public Random random() {
        return new Random();
    }


}
