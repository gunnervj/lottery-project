package com.bbb.lottery.lottery.config.xray;

import com.amazonaws.xray.proxies.apache.http.HttpClientBuilder;
import feign.Client;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientXrayConfig {

    @Bean
    public Client client(HttpClientConnectionManager httpClientConnectionManager,
                         FeignHttpClientProperties httpClientProperties) {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(httpClientProperties.getConnectionTimeout())
                .setRedirectsEnabled(httpClientProperties.isFollowRedirects())
                .build();

        return new ApacheHttpClient(HttpClientBuilder.create()
                                        .setConnectionManager(httpClientConnectionManager)
                                        .setDefaultRequestConfig(requestConfig).build());
    }
}
