package com.example.kongapiservice.network.reponse;

import com.google.gson.annotations.SerializedName;

public class ImageResponse {
    @SerializedName("data")
    public Data data;
    @SerializedName("status")
    public int status ;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class Data {
        @SerializedName("url")
        public String url;

        public String getUrl() {
            return url;
        }
    }
}
