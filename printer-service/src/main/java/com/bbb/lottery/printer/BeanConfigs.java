package com.bbb.lottery.printer;

import com.google.zxing.MultiFormatWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class BeanConfigs {
    @Bean
    public MultiFormatWriter multiFormatWriter() {
        return new MultiFormatWriter();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder().build();
    }
}
