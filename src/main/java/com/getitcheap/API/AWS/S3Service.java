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

    public boolean putObject(String filename, File file) {
        try {
            s3.putObject(BUCKET_NAME, filename, file);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteObject(String key) {
        try {
            s3.deleteObject(BUCKET_NAME, key);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
