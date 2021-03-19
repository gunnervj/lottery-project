package com.bbb.lottery.lottery.service.clients.printer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(url="${service.printer.url}", name="printer-service")
public interface PrinterServiceClient {

    @PostMapping("/print")
    public PrintResponse print(PrintRequest request);

}
