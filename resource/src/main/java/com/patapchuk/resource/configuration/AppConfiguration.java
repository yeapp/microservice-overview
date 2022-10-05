package com.patapchuk.resource.configuration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
@PropertySource("classpath:application.yml")
public class AppConfiguration {

    @Value("${song.storage.localstack}")
    private String endpoint;

    @Bean
    public AmazonS3 getAmazonS3Client() {
        log.info("Connecting to Amazon S3... {} ", endpoint);
        return AmazonS3Client
                .builder()
                .withClientConfiguration(clientConfiguration())
                .withEndpointConfiguration(new AwsClientBuilder
                        .EndpointConfiguration(endpoint, Regions.US_EAST_1.getName()))
                .withPathStyleAccessEnabled(true)
                .build();
    }

    @Bean
    ClientConfiguration clientConfiguration() {
        var config = new ClientConfiguration();
        config.setUserAgentPrefix("aws-cli/localstack-workaround");
        return config;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
