package com.getitcheap.API.AWS;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class S3Service {

    @Autowired
    AmazonS3 s3;

    private final String BUCKET_NAME = "get-it-cheap";

    public boolean uploadFile(String filename, File file) {
        try {
            s3.putObject(BUCKET_NAME, filename, file);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
