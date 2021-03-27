package com.bbb.lottery.lottery.service.clients.ticket;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(url = "${serviceTicketHost}/ticketing/api/v1", name="ticket-service")
public interface TicketServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "/ticket")
    TicketResponse getTicket();
}
