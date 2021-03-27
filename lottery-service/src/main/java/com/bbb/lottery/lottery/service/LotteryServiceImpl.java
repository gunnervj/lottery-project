package com.bbb.lottery.lottery.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.bbb.lottery.lottery.LotteryMessage;
import com.bbb.lottery.lottery.api.beans.LotteryTicket;
import com.bbb.lottery.lottery.dao.LotteryDao;
import com.bbb.lottery.lottery.exceptions.LotteryUnknownException;
import com.bbb.lottery.lottery.service.clients.printer.PrintRequest;
import com.bbb.lottery.lottery.service.clients.printer.PrintResponse;
import com.bbb.lottery.lottery.service.clients.printer.PrinterServiceClient;
import com.bbb.lottery.lottery.service.clients.ticket.TicketResponse;
import com.bbb.lottery.lottery.service.clients.ticket.TicketServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@XRayEnabled
public class LotteryServiceImpl implements LotteryService {

    private final TicketServiceClient ticketServiceClient;
    private final PrinterServiceClient printerServiceClient;
    private final LotteryDao lotteryDao;
    @Autowired
    public LotteryServiceImpl(TicketServiceClient ticketServiceClient,
                              PrinterServiceClient printerServiceClient,
                              LotteryDao lotteryDao) {
        this.ticketServiceClient = ticketServiceClient;
        this.printerServiceClient = printerServiceClient;
        this.lotteryDao = lotteryDao;
    }

    @Override
    public LotteryTicket buyLottery() {
        LotteryTicket.LotteryTicketBuilder responseBuilder = LotteryTicket.builder();
        String lotteryNumber = getTicketNumber();
        log.info("Generated Number is " + lotteryNumber) ;
        UUID uuid = UUID.randomUUID();
        String ticketLocation = printLottery(uuid.toString(), lotteryNumber);
        responseBuilder.location(ticketLocation);
        lotteryDao.saveLottery(uuid.toString(), lotteryNumber);
        return responseBuilder.build();
    }

    private String getTicketNumber() {
        try {
            TicketResponse ticketResponse = ticketServiceClient.getTicket();
            if (null != ticketResponse && !StringUtils.isEmpty(ticketResponse.getTicketNumber())) {
                return ticketResponse.getTicketNumber();
            }
        } catch (Exception ex) {
           log.error("Error while getting ticket number. Error : " + ex.getMessage(), ex);
            throw new LotteryUnknownException(LotteryMessage.UNKNOWN_ERROR.getMessage());
        }

        return "";
    }

    private String printLottery(String lotteryId, String lotteryNumber) {
        try {
            PrintRequest request = PrintRequest.builder()
                                                .lotteryId(lotteryId)
                                                .lotteryNumber(lotteryNumber)
                                                .build();
            PrintResponse printResponse = printerServiceClient.print(request);
            if (null != printResponse && !StringUtils.isEmpty(printResponse.getLocation())) {
                return printResponse.getLocation();
            }
        } catch (Exception ex) {
            log.error("Error while printing the lottery ticket. Error : " + ex.getMessage(), ex);
            throw new LotteryUnknownException(LotteryMessage.UNKNOWN_ERROR.getMessage());
        }
        return "";
    }
}
