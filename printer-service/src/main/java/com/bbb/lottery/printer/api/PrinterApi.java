package com.bbb.lottery.printer.api;

import com.bbb.lottery.printer.api.beans.PrintRequest;
import com.bbb.lottery.printer.api.beans.PrintResponse;

public interface PrinterApi {

    PrintResponse print(PrintRequest request);

}
