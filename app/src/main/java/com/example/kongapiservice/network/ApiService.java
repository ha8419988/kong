package com.example.kongapiservice.network;

import com.example.kongapiservice.network.reponse.LogInResponse;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    HttpLoggingInterceptor loggingInterCepter = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient.Builder okBuilder = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(loggingInterCepter);

    ApiService apiService = new Retrofit.Builder().baseUrl("http://192.168.1.120:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okBuilder.build())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())

            .build()
            .create(ApiService.class);

    @POST("/userServices/login")
    Single<LogInResponse> login(@Query(value = "email") String email, @Query(value = "password") String password);

    @POST("/userServices/register")
    Single<LogInResponse> register(@Query(value = "email") String email, @Query(value = "password") String password,
                                   @Query(value = "name") String name);

}
