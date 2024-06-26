package com.example.kongapiservice.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kongapiservice.R;
import com.example.kongapiservice.network.ApiService;
import com.example.kongapiservice.network.reponse.LogInResponse;
import com.example.kongapiservice.network.request.RegisterRequest;

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
        EditText edtUserName = findViewById(R.id.inputUserName);
        EditText edtName = findViewById(R.id.inputName);
        EditText edtPassword = findViewById(R.id.inputPassword);

        btnRegis.setOnClickListener(view -> {
            Call<LogInResponse> call = ApiService.apiService.register(new RegisterRequest(edtUserName.getText().toString(),
                    edtName.getText().toString(), edtPassword.getText().toString()));
            call.enqueue(new Callback<LogInResponse>() {
                @Override
                public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response) {
                    Toast.makeText(RegisterActivity.this, "Dang ky thanh cong", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Call<LogInResponse> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        });
    }
}