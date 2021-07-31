package com.getitcheap.API.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtResponse {

    private String jwt;

    private Long userId;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    public JwtResponse(String jwt, Long userId, String firstName, String lastName, String userName, String email) {
        this.jwt = jwt;
        this.userId = userId;
        this.username = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return  firstName;
    }

    public String setFirstName(String firstName) {
        return  this.firstName = firstName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return  lastName;
    }

    public String setLastName(String lastName) {
        return  this.lastName = lastName;
    }


    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
