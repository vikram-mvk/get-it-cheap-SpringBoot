package com.getitcheap.API.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class UserEntity implements UserDetails {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String password;

    private int active = 1;

    public Long getId() {
        return id;
    }

    public UserEntity setId(long id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return this.firstName;
    }

    @JsonProperty("firstName")
    public UserEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return this.lastName;
    }

    @JsonProperty("lastName")
    public UserEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    @JsonIgnore
    public int getActive() {
        return active;
    }

    @JsonIgnore
    public UserEntity setActive(int active) {
        this.active = active;
        return this;
    }

    @JsonProperty("username")
    public String getFullName() {
        return this.firstName+" "+this.lastName;
    }

    // Below methods are needed by Spring Security to perform Authentication

    @JsonProperty("email")
    @Override // In our app we're using email to authenticate a user. So return email to SpringBoot
    public String getUsername() {
        return getEmail();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getActive() == 1;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return new ArrayList<SimpleGrantedAuthority>() {
            { new SimpleGrantedAuthority("USER"); }
        };
    }

    public RowMapper<UserEntity> getRowMapper() {
        return new RowMapper<UserEntity>() {

            @Override
            public UserEntity mapRow(ResultSet rs, int i) throws SQLException {
                UserEntity user = new UserEntity();
                user
                    .setId(rs.getLong("id"))
                    .setFirstName(rs.getString("firstName"))
                    .setLastName(rs.getString("lastName"))
                    .setEmail(rs.getString("email"))
                    .setPassword(rs.getString("password"))
                    .setActive(rs.getInt("active"));

                return user;
            }
        };
    }

    public @interface columns {
        String ID = "id";
        String FIRST_NAME = "firstName";
        String LAST_NAME = "lastName";
        String EMAIL = "email";
        String PASSWORD = "password";
        String ACTIVE = "active";
    }


}



