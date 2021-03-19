package com.bbb.ticket.api;

import com.bbb.ticket.api.bean.TicketResponse;
import com.bbb.ticket.service.TicketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ticketing/api/v1/ticket")
public class TicketController implements  TicketApi {
    private final TicketingService ticketingService;

    @Autowired
    public TicketController (TicketingService ticketingService) {
        this.ticketingService = ticketingService;
    }

    @Override
    @GetMapping
    public TicketResponse createTicket() {
        return ticketingService.generateTicket();
    }

}
