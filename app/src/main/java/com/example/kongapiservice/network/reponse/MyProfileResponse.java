package com.example.kongapiservice.network.reponse;

import com.google.gson.annotations.SerializedName;

public class MyProfileResponse {
    @SerializedName("avatarUrl")
    private String avatarUrl;
    @SerializedName("email")
    private String email;
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private String id;

    public MyProfileResponse(String avatarUrl, String email, String name, String id) {
        this.avatarUrl = avatarUrl;
        this.email = email;
        this.name = name;
        this.id = id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
