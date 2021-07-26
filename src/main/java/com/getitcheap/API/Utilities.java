package com.getitcheap.API;

import com.getitcheap.API.DTO.MessageResponse;
import com.getitcheap.API.Items.ItemEntity;
import com.getitcheap.API.Items.ItemRoutes;
import com.getitcheap.API.Users.UserRoutes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utilities {

    public static ResponseEntity<MessageResponse> getSomethingWentWrongResponse() {
        return ResponseEntity.status(500).body(new MessageResponse("Something went wrong. Please try again."));
    }

    public static boolean isRequestValid(String endpoint, Object request) {
        boolean isValid = false;
        switch (endpoint) {
            case UserRoutes.SIGNIN:
                break;
            case UserRoutes.SIGNUP:
                break;
            case ItemRoutes.NEW_ITEM:
                break;
            default:
                break;
        }
        return isValid;
    }

    public static File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

}
