package com.getitcheap.API.Items;
;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ItemEntity itemEntity;

    private final Logger logger = LoggerFactory.getLogger(ItemRepository.class);

    List<ItemEntity> getItems(String itemTypes, String categories) {
        try {
            String sql = "SELECT * FROM items WHERE itemType %s AND category %s AND active = 1";
            sql = String.format(sql, itemTypes, categories);
            return jdbcTemplate.query(sql, itemEntity.getRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            logger.error( "Error in getAllItems()\n" + e.getMessage());
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
            return null;
        }
    }

    boolean newItem(ItemEntity item) {
        try {
            String sql = "INSERT INTO items(itemName, description, category, itemType, image, price, rentalBasis, userId,"
                    + " username, contact, datePosted, active) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, DEFAULT, 1)";
            return jdbcTemplate.update(sql,
                    item.getItemName(), item.getDescription(), item.getCategory(), item.getItemType(), item.getImage(),
                    item.getPrice(), item.getRentalBasis(), item.getUserId(), item.getUsername(), item.getContact()) > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        } catch (Exception e) {
            logger.error( "Error in newItem()\n" + e.getMessage());
            return false;
        }
    }

}
