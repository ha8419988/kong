package com.example.kongapiservice.network.request;

public class NewProductRequest {
    String name;
    int price;
    String description;
    String imageURL;
    String categoryId;

    public NewProductRequest(String name, int price, String description, String imageURL, String categoryId) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageURL = imageURL;
        this.categoryId = categoryId;
    }
}
