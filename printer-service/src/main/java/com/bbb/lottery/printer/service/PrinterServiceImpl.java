package com.bbb.lottery.printer.service;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.bbb.lottery.printer.LotteryMessage;
import com.bbb.lottery.printer.api.beans.PrintRequest;
import com.bbb.lottery.printer.api.beans.PrintResponse;
import com.bbb.lottery.printer.exceptions.PrinterUnknownException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.time.Duration;
import java.io.File;

@Service
@Slf4j
@XRayEnabled
public class PrinterServiceImpl implements PrinterService {
    private final QRService qrService;
    private final File templateFile;
    private final String OUTPUT_PREFIX = "tmp\\";
    private final S3Client s3Client;
    private final String TEMPLATE_FILE_NAME = "ticket_template.png";

    @Value("${lottery.AWS_BUCKET_NAME:bbbox-lottery-bucket}")
    private String AWS_LOTTERY_BUCKET_NAME;
    @Value("${lottery.LOTTERY_TEMPLATE_S3_PATH:template/ticket_template.png}")
    private String LOTTERY_TEMPLATE_S3_PATH;
    @Value("${lottery.LOTTERY_TEMPLATE_S3_BUCKET:bbbox-lottery-bucket}")
    private String LOTTERY_TEMPLATE_S3_BUCKET;

    @Autowired
    public PrinterServiceImpl(QRService qrService, S3Client s3Client) {
        this.qrService = qrService;
        this.templateFile = new File(OUTPUT_PREFIX + TEMPLATE_FILE_NAME);
        this.s3Client = s3Client;
        init();
    }


    private void init() {
        File directory = new File(OUTPUT_PREFIX);
        if (! directory.exists()) {
            if (directory.mkdir()) {
                log.info("tmp directory did not exist, so created one.");
            }
        }

    }

    @PostConstruct
    private void postInit() {
        AWSXRay.beginSegment("printer-service-init");
        File templateFile = new File(OUTPUT_PREFIX +  TEMPLATE_FILE_NAME);
        if (!templateFile.exists()) {
            log.info("lottery template do not exist, fetching from s3 : " + LOTTERY_TEMPLATE_S3_PATH);
            readTicketTemplateFromS3Bucket(templateFile);
        }
        AWSXRay.endSegment();
    }

    private void readTicketTemplateFromS3Bucket(File templateFile) {
        try {
            log.info("Reading the template from S3 bucket: " + LOTTERY_TEMPLATE_S3_BUCKET);
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(LOTTERY_TEMPLATE_S3_BUCKET)
                    .key(LOTTERY_TEMPLATE_S3_PATH)
                    .build();
            s3Client.getObject(getObjectRequest,
                    ResponseTransformer.toFile(templateFile));
            log.info("Completed reading the template from S3");
        } catch (Exception ex) {
            log.error("Error while reading ticket template: Error : " + ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public PrintResponse print(PrintRequest request) {
        PrintResponse.PrintResponseBuilder responseBuilder = PrintResponse.builder();
        responseBuilder.lotteryId(request.getLotteryId());
        validate(request);
        String qrFilePath = qrService.generateQR(request.getLotteryId());
        String ticketFilePath = printTicketImage(qrFilePath, request.getLotteryNumber(), request.getLotteryId());
        File fileToBeUploaded = new File(ticketFilePath);
        URL signedUrl = uploadToS3(generateFileName(request.getLotteryId()), fileToBeUploaded);
        if (fileToBeUploaded.exists()) {
            fileToBeUploaded.delete();
        }
        responseBuilder.location(signedUrl);
        return responseBuilder.build();
    }


    private URL uploadToS3(String fileName, File fileToBeUploaded) {
        S3Client s3Client = S3Client.builder().build();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(AWS_LOTTERY_BUCKET_NAME)
                .key(fileName)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromFile(fileToBeUploaded));
        log.debug("Uploaded printed lottery to s3 bucket " + AWS_LOTTERY_BUCKET_NAME);
        return getPrintedLotteryDownloadURL(fileName);
    }

    private URL getPrintedLotteryDownloadURL(String fileName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(AWS_LOTTERY_BUCKET_NAME)
                .key(fileName)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(getObjectRequest)
                .build();
        S3Presigner preSigner = S3Presigner.builder().build();
        PresignedGetObjectRequest presignedGetObjectRequest = preSigner.presignGetObject(getObjectPresignRequest);
        return presignedGetObjectRequest.url();
    }

    private String printTicketImage(String qrFilePath, String lotteryNumber, String lotteryId) {
        Graphics templateImageGraphics = null;
        File QRFile = null;
        String filePath = "";
        try {
            QRFile = new File(qrFilePath);
            log.info("Reading QR file from path :" +qrFilePath);
            BufferedImage qrBufferedImage = ImageIO.read(QRFile);
            BufferedImage templateBufferedImage = ImageIO.read(templateFile);
            templateImageGraphics = templateBufferedImage.getGraphics();
            templateImageGraphics.drawImage(qrBufferedImage, 15, 23, null);
            templateImageGraphics.setFont(new Font("Arial", Font.BOLD, 40));
            templateImageGraphics.setColor(Color.BLACK);
            templateImageGraphics.drawString(lotteryNumber, 290, 190);
            File output = new File(OUTPUT_PREFIX + generateFileName(lotteryId));
            ImageIO.write(templateBufferedImage,"PNG", output);
            filePath = output.getPath();
        } catch (Exception ex) {
            log.error("Error while printing ticket image. Error :" + ex.getMessage(), ex);
            throw new PrinterUnknownException(LotteryMessage.UNKNOWN_ERROR.getMessage());
        } finally {
            cleanup(templateImageGraphics, QRFile);
        }
        return filePath;
    }

    private void cleanup(Graphics templateImageGraphics, File QRFile) {
        if (null != templateImageGraphics) {
            templateImageGraphics.dispose();
        }
        if (null != QRFile) {
            if (QRFile.delete()) {
                log.debug("QR file cleaned up successfully.");
            } else {
                log.warn("Failed to cleanup the qr file.");
            }
        }
    }

    private void validate(PrintRequest request) {
        if (null == request) {
            throw new IllegalArgumentException("Invalid Request.");
        }

        if (StringUtils.isEmpty(request.getLotteryId())) {
            log.error("lotteryId=>" + request.getLotteryId());
            throw new IllegalArgumentException("Lottery ID cannot be empty.");
        }

        if (StringUtils.isEmpty(request.getLotteryNumber())) {
            throw new IllegalArgumentException("Lottery Number cannot be empty.");
        }
    }

    private String generateFileName(String lotteryId) {
        return lotteryId + ".png";
    }

    @PreDestroy
    private void cleanUp() {
        s3Client.close();
    }

}
