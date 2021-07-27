package com.getitcheap.API.Items;
;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class ItemRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ItemEntity itemEntity;

    private final Logger logger = LoggerFactory.getLogger(ItemRepository.class);

    List<ItemEntity> getItems(boolean hasLocationFilter, String itemTypes, String categories, String itemCities,
                              String itemStates, String itemZipCodes, String itemCountries) {
        try {
            String sql;
            if (hasLocationFilter) {
                sql = "SELECT * FROM items WHERE itemType %s AND category %s AND active = 1 AND addressId " +
                        "IN (select id from address WHERE city %s AND state %s AND zipcode %s AND country %s)";
                sql = String.format(sql, itemTypes, categories, itemCities, itemStates, itemZipCodes, itemCountries);
            } else {
                sql = "SELECT * FROM items WHERE itemType %s AND category %s AND active = 1";
                sql = String.format(sql, itemTypes, categories);
            }
            return jdbcTemplate.query(sql, itemEntity.getRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            logger.error( "Error in getAllItems()\n" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    List<ItemEntity> getItemsOfThisUser(Long userId) {
        try {
            String sql = "SELECT * FROM items WHERE userId = ? AND active = 1";
            return jdbcTemplate.query(sql, itemEntity.getRowMapper(), userId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            logger.error( "Error in getItemsOfThisUser()\n" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    ItemEntity getItem(Long id) {
        try {
            String sql = "SELECT * FROM items WHERE id = ? AND active = 1";
            return jdbcTemplate.queryForObject(sql, itemEntity.getRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            logger.error( "Error in getItem()\n" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    List<ItemEntity> searchItems(String searchKey) {
        try {
            String sql = "SELECT * FROM items WHERE itemName LIKE '%s' OR description LIKE '%s' OR " +
                    "category LIKE '%s' AND active = 1";

            searchKey = "%"+searchKey+"%";
            sql = String.format(sql, searchKey, searchKey, searchKey);
            return jdbcTemplate.query(sql, itemEntity.getRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            logger.error( "Error in searchItems()\n" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    boolean newItem(ItemEntity item) {
        try {
            String[] location = new String[5];
            int i =0;
            for (String addressPart :item.getItemLocation().split(",")) {
                addressPart = addressPart.strip();
                if(i==2) {
                    String temp[] = addressPart.split(" ");
                    String state = temp[0].strip();
                    String zipcode =  temp[1].strip();
                    location[i] = state;
                    location [i+1] =zipcode;
                    i+=2;
                } else {
                    location[i] = addressPart;
                    i++;
                }
            }
            int addressId = -1;
            // VALUES (id, houseAddress, street, city, state, zipcode, country)
            String insertAddressSQL = "INSERT INTO address VALUES (DEFAULT, '%s', '%s', '%s', '%s', '%s')";
            insertAddressSQL = String.format(insertAddressSQL, location[0], location[1], location[2], location[3], location[4]);
            boolean addressInsetSuccess = jdbcTemplate.update(insertAddressSQL) > 0;
            String lastInsertedIdSQL = "SELECT max(id) FROM address WHERE itemLocation LIKE '%s'";
            lastInsertedIdSQL = String.format(lastInsertedIdSQL, location[0]);
            addressId = jdbcTemplate.queryForObject(lastInsertedIdSQL, Integer.class);

            String sql = "INSERT INTO items(itemName, description, category, itemType, image, price, rentalBasis, userId,"
                    + " username, contact, itemLocation, addressId, datePosted, active) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, DEFAULT, 1)";

            return addressInsetSuccess && (jdbcTemplate.update(sql,
                    item.getItemName(), item.getDescription(), item.getCategory(), item.getItemType(), item.getImage(),
                    item.getPrice(), item.getRentalBasis(), item.getUserId(), item.getUsername(), item.getContact(),
                    item.getItemLocation(), addressId) > 0);
        } catch (EmptyResultDataAccessException e) {
            return false;
        } catch (Exception e) {
            logger.error( "Error in newItem()\n" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
