package com.bbb.ticket.config;

import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class WebConfig {

    private @Value("${spring.application.name:ticket-service}") String applicationName;

    @Bean
    public Filter TracingFilter() {
        return new AWSXRayServletFilter(applicationName);
    }

}
