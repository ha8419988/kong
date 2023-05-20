package com.example.kongapiservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kongapiservice.network.ApiService;
import com.example.kongapiservice.network.reponse.LogInResponse;
import com.example.kongapiservice.register.RegisterActivity;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
            ApiService.apiService.login("hoanganh84981@example.com", "11111111").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<LogInResponse>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
//                            Toast.makeText(MainActivity.this,"okay",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                        }

                        @Override
                        public void onSuccess(@NonNull LogInResponse logInResponse) {
                            Toast.makeText(LoginActivity.this, "okay1", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        });
        tvRegister.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

    }
}