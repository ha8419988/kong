package com.example.kongapiservice.myprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kongapiservice.R;
import com.example.kongapiservice.network.ApiService;
import com.example.kongapiservice.network.reponse.MyProfileResponse;
import com.example.kongapiservice.ui.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends AppCompatActivity {
    ImageView imgBack, imgAvatar;
    TextView tvName, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        findViewSID();
        getApiProfile();

    }

    private void getApiProfile() {
        SharedPreferences prefs = getSharedPreferences(Constant.SHARED_PREFERENCES, MODE_PRIVATE);
        String id = prefs.getString(Constant.ID_USER, "");//"No name defined" is the default value.

        Call<MyProfileResponse> call = ApiService.apiService.getProfile(id);
        call.enqueue(new Callback<MyProfileResponse>() {
            @Override
            public void onResponse(Call<MyProfileResponse> call, Response<MyProfileResponse> response) {
//                Glide.with(MyProfileActivity.this).load(response.body().getAvatarUrl()).into(imgAvatar);
            }

            @Override
            public void onFailure(Call<MyProfileResponse> call, Throwable t) {

            }
        });
    }

    private void findViewSID() {
        imgBack = findViewById(R.id.imgBack);
        imgAvatar = findViewById(R.id.imgAvatar);


        tvName = findViewById(R.id.name);
        tvEmail = findViewById(R.id.email);
        imgBack.setOnClickListener(view -> finish());
    }
}