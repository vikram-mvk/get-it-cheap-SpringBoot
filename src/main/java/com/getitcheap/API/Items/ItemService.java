package com.getitcheap.API.Items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    public ItemEntity getItem(Long id) {
       return itemRepository.getItem(id);
    }

    public List<ItemEntity> getItems(List<String> itemTypes, List<String> itemCategories) {
        String itemTypesQuery = "";
        String itemCategoriesQuery = "";
        String queryFormat = ",'%s'";

        try {
            if (itemTypes == null || itemTypes.size() == 0) {
                itemTypesQuery = "LIKE '%'";
            } else {

                for (String type : itemTypes) {
                    itemTypesQuery += String.format(queryFormat, type);
                }
                itemTypesQuery = itemTypesQuery.substring(1);
                itemTypesQuery = String.format("IN (%s)", itemTypesQuery);
            }

            if (itemCategories == null || itemCategories.size() == 0) {
                itemCategoriesQuery = "LIKE '%'";
            } else {

                for (String category : itemCategories) {
                    itemCategoriesQuery += String.format(queryFormat, category);
                }
                itemCategoriesQuery = itemCategoriesQuery.substring(1);
                itemCategoriesQuery = String.format("IN (%s)", itemCategoriesQuery);
            }
        } catch (Exception e) {
            return null;
        }
        return itemRepository.getItems(itemTypesQuery, itemCategoriesQuery);
    }

    public List<ItemEntity> getUserItems(Long id) {
        return itemRepository.getItemsOfThisUser(id);
    }

    public boolean newItem(ItemEntity item) {
        return itemRepository.newItem(item);
    }

    public List<ItemEntity> searchItems(String searchKey) {
        if (searchKey.isEmpty()) {
            return null;
        }
        return itemRepository.searchItems(searchKey);
    }



}
