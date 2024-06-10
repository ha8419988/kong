package com.example.kongapiservice.network;


import com.example.kongapiservice.login.LoginActivity;
import com.example.kongapiservice.network.reponse.CategoryListResponse;
import com.example.kongapiservice.network.reponse.ImageResponse;
import com.example.kongapiservice.network.reponse.MyProfileResponse;
import com.example.kongapiservice.network.request.EditProfileRequest;
import com.example.kongapiservice.network.request.LoginRequest;
import com.example.kongapiservice.network.reponse.LogInResponse;
import com.example.kongapiservice.network.request.NewCategoryRequest;
import com.example.kongapiservice.network.request.NewProductRequest;
import com.example.kongapiservice.network.request.RegisterRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    HttpLoggingInterceptor loggingInterCepter = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient.Builder okBuilder = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true).addInterceptor(chain -> {
                Request.Builder newRequest = chain.request().newBuilder();
                return chain.proceed(newRequest.build());
            })
            .addInterceptor(loggingInterCepter);
    //Cong ty:      http://172.168.10.211:8000
    //Nha :    http://192.168.1.120:8000
    //http://192.168.1.187:8000
    ApiService apiService = new Retrofit.Builder().baseUrl("http://192.168.1.120:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okBuilder.build())

            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(ApiService.class);
    ApiService apiServiceUpload = new Retrofit.Builder().baseUrl("https://api.imgbb.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okBuilder.build())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(ApiService.class);

    @POST("/userServices/login")
    Call<LogInResponse> login(@Body LoginRequest request);

    @POST("/userServices/register")
    Call<LogInResponse> register(@Body RegisterRequest request);

    @GET("/productServices/category")
    Call<List<CategoryListResponse>> getCategoryList();

    @GET("/productServices/category/{id}")
    Call<CategoryListResponse> getCategoryDetail(@Path("id") String id);

    @GET("/userServices/{id}")
    Call<MyProfileResponse> getProfile(@Path("id") String id);

    @PUT("/userServices/{id}")
    Call<CategoryListResponse> editProfile(@Path("id") String id, @Body EditProfileRequest request);

    @Multipart
    @POST("/1/upload?key=d84f65c591033db16ff03258c20dd654")
    Call<ImageResponse> postImage(@Part MultipartBody.Part image);

    @POST("/productServices/category")
    Call<CategoryListResponse> postCategory(@Body NewCategoryRequest request);

    @DELETE("/productServices/category/{id}")
    Call<ImageResponse> removeCategory(@Path("id") String idCategory);

    @PUT("/productServices/category/{id}")
    Call<ImageResponse> updateCategory(@Path("id") String idCategory, @Body NewCategoryRequest request);

    @POST("/productServices")
    Call<CategoryListResponse> postProduct(@Body NewProductRequest request);

    @PUT("/productServices/{id}")
    Call<CategoryListResponse> updateProduct(@Path("id") String id, @Body NewProductRequest request);

    @DELETE("/productServices/{id}")
    Call<ImageResponse> removeProduct(@Path("id") String id);
}
