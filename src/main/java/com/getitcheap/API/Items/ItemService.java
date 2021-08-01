package com.getitcheap.API.Items;

import com.getitcheap.API.AWS.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    S3Service s3Service;

    public ItemEntity getItem(Long id) {
       return itemRepository.getItem(id);
    }

    public List<ItemEntity> getItems(List<String> itemTypes, List<String> itemCategories, List<String> itemCities,
                                     List<String> itemStates, List<String> itemZipCodes, List<String> itemCountries) {
        String itemTypesQuery, itemCategoriesQuery, itemCitiesQuery, itemStatesQuery, itemCountriesQuery, itemZipCodesQuery;
        boolean hasLocationFilter = false;

        try {
            itemTypesQuery = getSqlQueryString(itemTypes);
            itemCategoriesQuery = getSqlQueryString(itemCategories);
            itemCitiesQuery = getSqlQueryString(itemCities);
            itemStatesQuery = getSqlQueryString(itemStates);
            itemZipCodesQuery = getSqlQueryString(itemZipCodes);
            itemCountriesQuery = getSqlQueryString(itemCountries);
            hasLocationFilter = itemCities != null || itemCountries != null || itemStates != null || itemZipCodes != null;
        } catch (Exception e) {
            return null;
        }
        return itemRepository.getItems(hasLocationFilter, itemTypesQuery, itemCategoriesQuery, itemCitiesQuery,
                itemStatesQuery, itemZipCodesQuery, itemCountriesQuery);
    }

    public List<ItemEntity> getUserItems(Long id) {
        return itemRepository.getItemsOfThisUser(id);
    }

    public boolean newItem(ItemEntity item) {
        return itemRepository.newItem(item);
    }

    public boolean deleteItems(List<Long> ids) {
        boolean success = true;
        List<ItemEntity> deletedItems = itemRepository.deleteItems(ids);
        if (deletedItems.isEmpty()) {
            success = false;
        } else {
            for (ItemEntity item : deletedItems) {
                success = success & s3Service.deleteObject(item.getImage());
            }
        }
        return success;
    }

    public List<ItemEntity> searchItems(String searchKey) {
        if (searchKey.isEmpty()) {
            return null;
        }
        return itemRepository.searchItems(searchKey);
    }


    private String getSqlQueryString(List<String> locationFilterValues) {
        String queryNoFilter = "LIKE '%'";
        if (locationFilterValues == null || locationFilterValues.size() ==0) {
            return queryNoFilter;
        }
        String query = "";
        String queryFormat = ",'%s'";

        for (String filterValue : locationFilterValues) {
            if (filterValue.equalsIgnoreCase("All")) { return queryNoFilter;}
            // String filter = filterValue.replace("%20", " ");
            query += String.format(queryFormat, filterValue);
        }
        query = query.substring(1);
        query = String.format("IN (%s)", query);

        return query;
    }

}
