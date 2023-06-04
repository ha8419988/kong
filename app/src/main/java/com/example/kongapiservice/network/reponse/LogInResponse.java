package com.example.kongapiservice.network.reponse;

import com.google.gson.annotations.SerializedName;

public class LogInResponse {
    @SerializedName("token")
    private String token;
    @SerializedName("email")
    private String email;
    @SerializedName("name")
    private String name;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
