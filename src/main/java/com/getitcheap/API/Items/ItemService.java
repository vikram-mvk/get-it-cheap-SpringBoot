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

    public List<ItemEntity> searchItems(String searchKey) {
        if (searchKey.isEmpty()) {
            return null;
        }
        return itemRepository.searchItems(searchKey);
    }


    private String getSqlQueryString(List<String> locationFilterValues) {
        String queryNoFilter = "LIKE '%'";
        String query = "";
        String queryFormat = ",'%s'";

        if (locationFilterValues != null && locationFilterValues.size() > 0) {
            for (String filterValue : locationFilterValues) {
                if (filterValue.equals("All")) {
                    return queryNoFilter;
                }
                query += String.format(queryFormat, filterValue);
            }
            query = query.substring(1);
            query = String.format("IN (%s)", query);
        } else {
            query = queryNoFilter;
        }
        return query;
    }

}
