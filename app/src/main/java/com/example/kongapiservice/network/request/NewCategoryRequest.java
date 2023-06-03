package com.example.kongapiservice.network.request;

public class NewCategoryRequest {
    String name;
    String imageURL;

    public NewCategoryRequest(String name, String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
    }
}
