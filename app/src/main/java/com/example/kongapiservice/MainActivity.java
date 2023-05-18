package com.example.kongapiservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kongapiservice.register.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewByIdS();
    }

    private void findViewByIdS() {
        EditText edtUser = findViewById(R.id.inputUserName);
        EditText edtPass = findViewById(R.id.inputPassword);
        TextView tvRegister = findViewById(R.id.tvRegister);
        Button btnLogin = findViewById(R.id.btnLogin);
        String textUser = edtUser.getText().toString();
        String textPass = edtUser.getText().toString();


        btnLogin.setOnClickListener(view -> {

        });
        tvRegister.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });

    }
}