package com.example.user_rest_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDto {

    @JsonProperty(value = "user_id")
    private int userId;

    @JsonProperty(value = "user_name")
    private String userName;

    @JsonProperty(value = "user_age")
    private String userAge;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }
}
