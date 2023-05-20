package com.example.kongapiservice.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.kongapiservice.R;
import com.example.kongapiservice.network.ApiService;
import com.example.kongapiservice.network.reponse.LogInResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewByIds();
    }

    private void findViewByIds() {
        Button btnRegis=findViewById(R.id.btnRegis);

        btnRegis.setOnClickListener(view -> {
            ApiService.apiService.register("hoanganh84982@example.com","11111111","Hoàng ANh").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<LogInResponse>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            Toast.makeText(RegisterActivity.this,"okay",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(@NonNull LogInResponse logInResponse) {
                            Toast.makeText(RegisterActivity.this,"okay1",Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }
                    });
        });
    }
}