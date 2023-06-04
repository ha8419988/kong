package com.example.kongapiservice.network.request;

public class NewProductRequest {
    String name;
    int prices;
    String description;
    String imageURL;
    String categoryId;

    public NewProductRequest(String name, int prices, String description, String imageURL, String categoryId) {
        this.name = name;
        this.prices = prices;
        this.description = description;
        this.imageURL = imageURL;
        this.categoryId = categoryId;
    }
}
