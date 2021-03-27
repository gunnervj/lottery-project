package com.bbb.ticket.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.bbb.ticket.api.bean.TicketResponse;
import com.bbb.ticket.exceptions.TicketingUnknownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@XRayEnabled
public class TicketingServiceImpl implements  TicketingService {
    private final Random randomDigitGenerator;

    @Value("${lowerLimit: 1}")
    private int lowerLimit;
    @Value("${upperLimit: 60}")
    private int upperLimit;
    @Value("${numberCount: 8}")
    private int numberCount;

    @Autowired
    public TicketingServiceImpl(Random random) {
        this.randomDigitGenerator = random;
    }

    @Override
    public TicketResponse generateTicket() {
        TicketResponse.TicketResponseBuilder responseBuilder = TicketResponse.builder();
        try {
            String number = randomDigitGenerator.ints(numberCount,
                    lowerLimit,
                    upperLimit)
                    .boxed()
                    .map(String::valueOf)
                    .collect(Collectors.joining(" "));
            responseBuilder.ticketNumber(number);
        } catch (Exception ex) {
            log.error("Error While generating Lottery Ticket : " + ex.getMessage(), ex);
            throw new TicketingUnknownException("Error happened while generating lottery ticket. Please try again");
        }

        return responseBuilder.build();
    }
}
