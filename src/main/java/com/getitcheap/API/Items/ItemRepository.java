package com.getitcheap.API.Items;
;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
                sql = "SELECT * FROM items WHERE itemType %s AND category %s AND active = 1 AND id " +
                        "IN (select itemId from address WHERE city %s AND state %s AND zipcode %s AND country %s)";
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

    List<ItemEntity> deleteItems(List<Long> ids) {
        List<ItemEntity> deletedItems = new ArrayList<>();
        if (ids == null) {return deletedItems; }
        boolean success = true;
        String sql = "DELETE from items where id = ?";
        String getItemSql = "SELECT * from items where id = ?";
        try {

            for (Long id : ids) {
                ItemEntity deletedItem =  jdbcTemplate.query(getItemSql, itemEntity.getRowMapper()).get(0);
                success = success && (jdbcTemplate.update(sql, id) > 0);
                if (!success) {
                    break;
                } else {
                    deletedItems.add(deletedItem);
                }
            }
            return deletedItems;
        } catch (EmptyResultDataAccessException e) {
            return deletedItems;
        } catch (Exception e) {
            logger.error("Error in newItem()\n" + e.getMessage());
            e.printStackTrace();
            return deletedItems;
        }
    }

    boolean newItem(ItemEntity item) {

        try {
            // First insert the item
            String insertItemSql = "INSERT INTO items(itemName, description, category, itemType, image, price, rentalBasis, userId,"
                    + " username, contact, itemLocation, datePosted, active) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, DEFAULT, 1)";

            boolean itemInsertSuccess = jdbcTemplate.update(insertItemSql,
                    item.getItemName(), item.getDescription(), item.getCategory(), item.getItemType(), item.getImage(),
                    item.getPrice(), item.getRentalBasis(), item.getUserId(), item.getUsername(), item.getContact(),
                    item.getItemLocation()) > 0;

            try { // Inserting address in table is optional. Only filter won't work for this item, if it fails.
                if (itemInsertSuccess) {
                    String[] addressParts = item.getItemLocation().strip().split(",");
                    String country = null;
                    String state = null;
                    String zipcode = null;
                    String city = null;
                    String localityOrHouseAddress = null;
                    try {
                        // Try to parse address
                        country = addressParts[addressParts.length-1].strip();
                        String[] temp = addressParts[addressParts.length -2].strip().split(" ");
                        state = temp[0];
                        if (temp.length == 2) {
                            zipcode = temp[1];
                        }
                        city = addressParts[addressParts.length -3].strip();
                        localityOrHouseAddress = addressParts[addressParts.length - 4].strip();
                    } catch (Exception e) {
                        // ignore
                    }
                    String itemIdSql = "SELECT max(id) FROM items WHERE userId = ?";
                    int itemId = jdbcTemplate.queryForObject(itemIdSql, Integer.class, new Object[]{item.getUserId()});
                    String insertAddressSQL = "INSERT INTO address VALUES (DEFAULT, %d, '%s', '%s', '%s', '%s', '%s')";
                    insertAddressSQL = String.format(insertAddressSQL, itemId, localityOrHouseAddress, city, state,
                            zipcode, country);
                    jdbcTemplate.update(insertAddressSQL);
                }
            } catch (Exception e) {
                logger.error( "Error in newItem() addressInsertion\n" + e.getMessage());
                e.printStackTrace();
            }
            return itemInsertSuccess;

        } catch (EmptyResultDataAccessException e) {
            return false;
        } catch (Exception e) {
            logger.error( "Error in newItem()\n" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
