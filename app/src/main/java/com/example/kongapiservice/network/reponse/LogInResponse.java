package com.example.kongapiservice.network.reponse;

import com.google.gson.annotations.SerializedName;

public class LogInResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private Data data;

    public String getStatus() {
        return status;
    }

    public Data getData() {
        return data;
    }

    public class Data {
        @SerializedName("access_token")
        private String accessToken;
        @SerializedName("user_id")
        private Long idUser;

        public String getAccessToken() {
            return accessToken;
        }

        public Long getIdUser() {
            return idUser;
        }
    }
}
