package com.github.sevtech.cloud.storage.spring.config.local;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.github.sevtech.cloud.storage.spring.config.ConditionalOnCloudStorageProperty;
import com.github.sevtech.cloud.storage.spring.property.aws.AwsS3Properties;
import com.github.sevtech.cloud.storage.spring.property.local.LocalProperties;
import com.github.sevtech.cloud.storage.spring.service.StorageService;
import com.github.sevtech.cloud.storage.spring.service.aws.AwsS3Service;
import com.github.sevtech.cloud.storage.spring.service.local.LocalStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;

@Slf4j
@Configuration
@ConditionalOnCloudStorageProperty(value = "storage.local.enabled")
public class LocalConfig {

    @Bean
    public LocalProperties localProperties() {
        return new LocalProperties();
    }

    @Bean
    public StorageService localService() {
        return new LocalStorageService();
    }

}
