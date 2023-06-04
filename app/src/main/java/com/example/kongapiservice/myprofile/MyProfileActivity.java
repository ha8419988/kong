package com.example.kongapiservice.myprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.kongapiservice.R;

public class MyProfileActivity extends AppCompatActivity {
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        findViewSID();

    }

    private void findViewSID() {
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(view -> finish());
    }
}