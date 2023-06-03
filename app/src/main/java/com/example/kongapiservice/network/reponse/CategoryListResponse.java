package com.example.kongapiservice.network.reponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryListResponse {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("imageURL")
    private String imageURL;
    @SerializedName("product")
    public List<Product> product;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public class Product {
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
        @SerializedName("prices")
        public String prices;
        @SerializedName("description")
        public String description;
        @SerializedName("imageURL")
        public String imageURL;

        public String getImageURL() {
            return imageURL;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrices() {
            return prices;
        }

        public void setPrices(String prices) {
            this.prices = prices;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
