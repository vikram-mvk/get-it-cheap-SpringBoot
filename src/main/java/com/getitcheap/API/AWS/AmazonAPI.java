package com.getitcheap.API.AWS;

import com.amazonaws.auth.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonAPI {

    @Bean
    public AmazonS3 amazonS3() {
        try {
        return AmazonS3ClientBuilder
                .standard()
                .withRegion("us-east-1")
                .withCredentials(new InstanceProfileCredentialsProvider(false))
                .build();
    } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}