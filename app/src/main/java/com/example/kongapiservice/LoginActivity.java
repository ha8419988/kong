package com.example.kongapiservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kongapiservice.network.ApiService;
import com.example.kongapiservice.network.reponse.LogInResponse;
import com.example.kongapiservice.network.request.LoginRequest;
import com.example.kongapiservice.register.RegisterActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
            Call<LogInResponse> call = ApiService.apiService.login(new LoginRequest("hoanganh84981@example.com", "11111111"));
            call.enqueue(new Callback<LogInResponse>() {
                @Override
                public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response) {
                    if (response.raw().code() == 200) {
                        new Handler().postDelayed(() ->
                                startActivity(new Intent(LoginActivity.this, MainActivity.class)), 500);
                    }
                }

                @Override
                public void onFailure(Call<LogInResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "fail", Toast.LENGTH_SHORT).show();

                }
            });
        });
        tvRegister.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

    }
}