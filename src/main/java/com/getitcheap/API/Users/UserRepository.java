package com.getitcheap.API.Users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserEntity userEntity;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    UserEntity findByEmail(String email) {
        try {
            String sql = "SELECT * FROM users WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, userEntity.getRowMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            logger.error( "Error in findByEmail()\n" + e.getMessage());
            return null;
        }
    }

    boolean existsByEmail(String email) {
        try {
            String sql = "SELECT EXISTS (SELECT * FROM users WHERE email = ?)";
            return jdbcTemplate.queryForObject(sql, Boolean.class, email);
        } catch (Exception e) {
            logger.error( "Error in existsByEmail()\n" + e.getMessage());
            return true;
        }
    }

    boolean signUp(String firstName, String lastName, String email, String password) {
        try {
            String sql = "INSERT INTO users(firstName, lastName, email, password, active) VALUES(?,?,?,?,1)";
            return jdbcTemplate.update(sql, firstName, lastName, email, password) > 0;
        } catch (Exception e) {
            logger.error( "Error in signUp()\n" + e.getMessage());
            return false;
        }
    }
}
