package com.bbb.lottery.printer.service;

import com.bbb.lottery.printer.LotteryMessage;
import com.bbb.lottery.printer.exceptions.PrinterUnknownException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@Slf4j
@Service
public class QRServiceImpl implements QRService {
    @Value("${qrcode.height:255}")
    private int qrCodeHeight;
    @Value("${qrcode.width:255}")
    private int qrCodeWidth;

    private final MultiFormatWriter multiFormatWriter;

    private static final String QR_IMAGE_PATH = "tmp\\QR_%s.png";

    @Autowired
    public QRServiceImpl(MultiFormatWriter multiFormatWriter) {
        this.multiFormatWriter = multiFormatWriter;
    }

    @Override
    public String generateQR(String message) {
        String imagePath = "";
        try {
            BitMatrix matrix = this.multiFormatWriter.encode(
                    message,
                    BarcodeFormat.QR_CODE, qrCodeWidth, qrCodeHeight);
            BufferedImage qrBuffImage = MatrixToImageWriter.toBufferedImage(matrix);
            File outputFile = new File(String.format(QR_IMAGE_PATH, message));
            ImageIO.write(qrBuffImage, "PNG", outputFile);
            imagePath = outputFile.getPath();
        } catch (Exception ex) {
            log.error("Error while generating QR Code:" + ex.getMessage(), ex);
            throw new PrinterUnknownException(LotteryMessage.UNKNOWN_ERROR.getMessage());
        }

        return imagePath;
    }
}
