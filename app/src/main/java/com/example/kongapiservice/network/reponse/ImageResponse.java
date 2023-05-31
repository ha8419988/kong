package com.example.kongapiservice.network.reponse;

import com.google.gson.annotations.SerializedName;

public class ImageResponse {

    @SerializedName("status")
    public int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
