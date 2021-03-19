package com.bbb.lottery.printer.service;

import com.bbb.lottery.printer.api.beans.PrintRequest;
import com.bbb.lottery.printer.api.beans.PrintResponse;

public interface PrinterService {
    PrintResponse print(PrintRequest request);
}
