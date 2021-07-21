package com.getitcheap.API.Items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ItemEntity {

    private Long id;

    private String itemName;

    private String description;

    private String category;

    private String itemType;

    private String image;

    private double price;

    private String rentalBasis;

    private Long userId;

    private String contact;

    private String datePosted;

    private int active;

    private String username;

    private String itemLocation;

    private int addressId;

    public Long getId() {
        return id;
    }

    public ItemEntity setId(Long id) {
        this.id = id;
        return this;
    }

    @JsonProperty("itemName")
    public String getItemName() {
        return itemName;
    }

    public ItemEntity setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public ItemEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    public ItemEntity setCategory(String category) {
        this.category = category;
        return this;
    }

    @JsonProperty("itemType")
    public String getItemType() {
        return itemType;
    }

    public ItemEntity setItemType(String itemType) {
        this.itemType = itemType;
        return this;
    }

    @JsonProperty("rentalBasis")
    public String getRentalBasis() {
        return rentalBasis;
    }

    public ItemEntity setRentalBasis(String rentalBasis) {
        this.rentalBasis = rentalBasis;
        return this;
    }

    @JsonProperty("itemLocation")
    public String getItemLocation() {
        return itemLocation;
    }

    public ItemEntity setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
        return this;
    }

    @JsonIgnore
    public int getAddressId() {
        return addressId;
    }

    public ItemEntity setAddressId(int addressId) {
        this.addressId = addressId;
        return this;
    }


    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    public ItemEntity setImage(String image) {
        this.image = image;
        return this;
    }

    @JsonProperty("price")
    public double getPrice() {
        return price;
    }

    public ItemEntity setPrice(double price) {
        this.price = price;
        return this;
    }

    @JsonProperty("userId")
    public Long getUserId() {
        return userId;
    }

    public ItemEntity setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    @JsonProperty("contact")
    public String getContact() {
        return contact;
    }

    public ItemEntity setContact(String contact) {
        this.contact = contact;
        return this;
    }

    @JsonProperty("datePosted")
    public String getDatePosted() {
        return datePosted;
    }

    public ItemEntity setDatePosted(String datePosted) {
        this.datePosted = datePosted;
        return this;
    }


    @JsonIgnore
    @JsonProperty("active")
    public int getActive() {
        return active;
    }

    public String getUsername() {
        return this.username;
    }

    public ItemEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    @JsonIgnore
    public ItemEntity setActive(int active) {
        this.active = active;
        return this;
    }

    @JsonIgnore
    public RowMapper<ItemEntity> getRowMapper() {
        return new RowMapper<ItemEntity>() {

            @Override
            public ItemEntity mapRow(ResultSet rs, int i) throws SQLException {
                ItemEntity item = new ItemEntity();
                item
                        .setId(rs.getLong("id"))
                        .setItemName(rs.getString("itemName"))
                        .setDescription(rs.getString("description"))
                        .setCategory(rs.getString("category"))
                        .setItemType(rs.getString("itemType"))
                        .setImage(rs.getString("image"))
                        .setPrice(rs.getDouble("price"))
                        .setRentalBasis(rs.getString("rentalBasis"))
                        .setUserId(rs.getLong("userId"))
                        .setUsername(rs.getString("username"))
                        .setContact(rs.getString("contact"))
                        .setItemLocation(rs.getString("itemLocation"))
                        .setDatePosted(rs.getString("datePosted"))
                        .setActive(rs.getInt("active"));

                return item;
            }
        };
    }

}
