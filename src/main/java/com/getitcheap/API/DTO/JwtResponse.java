package com.getitcheap.API.DTO;

public class JwtResponse {

    private String jwt;

    private Long userId;

    private String username;

    private String email;

    public JwtResponse(String jwt, Long userId, String userName, String email) {
        this.jwt = jwt;
        this.userId = userId;
        this.username = userName;
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
