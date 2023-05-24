package com.example.kongapiservice.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.kongapiservice.LoginActivity;
import com.example.kongapiservice.R;
import com.example.kongapiservice.network.ApiService;
import com.example.kongapiservice.network.reponse.LogInResponse;
import com.example.kongapiservice.network.request.LoginRequest;
import com.example.kongapiservice.network.request.RegisterRequest;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewByIds();
    }

    private void findViewByIds() {
        Button btnRegis = findViewById(R.id.btnRegis);

        btnRegis.setOnClickListener(view -> {
            Call<LogInResponse> call = ApiService.apiService.register(new RegisterRequest("hoanganh84@example.com", "11111111", "Hoang Anh"));
            call.enqueue(new Callback<LogInResponse>() {
                @Override
                public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response) {
                    Toast.makeText(RegisterActivity.this, "vao r nhe", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<LogInResponse> call, Throwable t) {

                }
            });

        });
    }
}