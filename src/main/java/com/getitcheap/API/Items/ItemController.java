package com.getitcheap.API.Items;


import com.amazonaws.endpointdiscovery.DaemonThreadFactory;
import com.getitcheap.API.AWS.S3Service;
import com.getitcheap.API.DTO.MessageResponse;
import com.getitcheap.API.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;

@CrossOrigin("*")
@RestController
public class ItemController {

    @Autowired
    ItemService itemService;

    @Autowired
    S3Service s3Service;

    @GetMapping(ItemRoutes.GET_ITEMS)
    public ResponseEntity<?> getItems(@RequestParam(value = "type", required = false) List<String> itemTypes,
                                      @RequestParam(value = "category", required = false) List<String> categories ) {

        List<ItemEntity> items = itemService.getItems(itemTypes, categories);
        return ResponseEntity.status(200).body(items == null ? new MessageResponse("No Items found") : items);
    }

    @GetMapping(ItemRoutes.SEARCH_ITEMS)
    public ResponseEntity<?> searchItems(@RequestParam(value = "key") String searchKey) {

        List<ItemEntity> items = itemService.searchItems(searchKey);
        return ResponseEntity.status(200).body(items == null ? new MessageResponse("No Items found") : items);
    }

    @GetMapping(ItemRoutes.GET_ITEM)
    public ResponseEntity<?> getItem(@PathVariable Long id) {

        ItemEntity item = itemService.getItem(id);
        return ResponseEntity.ok().body(item == null ? new MessageResponse("Item not found") : item);
    }

    @PostMapping(ItemRoutes.NEW_ITEM)
    public ResponseEntity<?> newItem(@RequestParam(value="image",required = false) MultipartFile image, HttpServletRequest req) {
        try {
            boolean success = false;
            ItemEntity item = new ItemEntity()
                    .setItemName(req.getParameter("itemName"))
                    .setDescription(req.getParameter("description"))
                    .setPrice(Double.parseDouble(req.getParameter("price")))
                    .setCategory(req.getParameter("category"))
                    .setItemType(req.getParameter("itemType"))
                    .setRentalBasis(req.getParameter("rentalBasis"))
                    .setContact(req.getParameter("contact"))
                    .setUsername(req.getParameter("username"))
                    .setUserId(Long.parseLong(req.getParameter("userId")));

            if (image != null) {
                File imageFile = Utilities.convertMultiPartToFile(image);
                String imageName = item.getItemName() + "_" + new Date().getTime() + ".jpeg";

                success = s3Service.uploadFile(imageName, imageFile);

                if (success) {
                    item.setImage(imageName);
                }
            }

            success = itemService.newItem(item);

            return success ? ResponseEntity.status(200).body(new MessageResponse("Successful")) :
                    Utilities.getSomethingWentWrongResponse();
        } catch (Exception e) {
            System.out.println(e);
        }

        return Utilities.getSomethingWentWrongResponse();
    }

    @GetMapping(ItemRoutes.USER_ITEM)
    public ResponseEntity<?> getUserItems(@PathVariable Long id) {
            List<ItemEntity> userItems = itemService.getUserItems(id);
        return ResponseEntity.status(200).body(userItems == null ? new MessageResponse("This user has not posted any items.")
                : userItems);
    }

}
