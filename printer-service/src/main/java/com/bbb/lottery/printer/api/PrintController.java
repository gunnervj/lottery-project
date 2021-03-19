package com.bbb.lottery.printer.api;

import com.bbb.lottery.printer.api.beans.PrintRequest;
import com.bbb.lottery.printer.api.beans.PrintResponse;
import com.bbb.lottery.printer.service.PrinterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("printer/api/v1")
public class PrintController implements PrinterApi {
    private final PrinterService printerService;

    @Autowired
    public PrintController(PrinterService printerService) {
        this.printerService = printerService;
    }

    @Override
    @PostMapping("/print")
    public PrintResponse print(@RequestBody PrintRequest request) {
        return  printerService.print(request);
    }
}
