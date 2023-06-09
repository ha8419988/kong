package com.example.kongapiservice.network;

import androidx.annotation.NonNull;

import com.example.kongapiservice.network.reponse.CategoryListResponse;
import com.example.kongapiservice.network.reponse.ImageResponse;
import com.example.kongapiservice.network.request.EditProfileRequest;
import com.example.kongapiservice.network.request.ImageRequest;
import com.example.kongapiservice.network.request.LoginRequest;
import com.example.kongapiservice.network.reponse.LogInResponse;
import com.example.kongapiservice.network.request.RegisterRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    HttpLoggingInterceptor loggingInterCepter = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient.Builder okBuilder = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)

            .retryOnConnectionFailure(true).addInterceptor(new Interceptor() {
                @NonNull
                @Override
                public Response intercept(@NonNull Chain chain) throws IOException {
                    Request.Builder newRequest = chain.request().newBuilder();

                    newRequest.addHeader("Content-Type", "multipart/form-data;boundary=<calculated when request is sent>");
//                    newRequest.addHeader("Content-Length", "calculated when request is sent");
//                    newRequest.addHeader("Host", "calculated when request is sent");
//                    newRequest.addHeader("User-Agent", getUserAgent());
                    return chain.proceed(newRequest.build());

                }
            })
            .addInterceptor(loggingInterCepter);

    ApiService apiService = new Retrofit.Builder().baseUrl("http://172.168.10.211:8000")
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
    Call<CategoryListResponse> getProfile(@Path("id") String id);

    @PUT("/userServices/{id}")
    Call<CategoryListResponse> editProfile(@Path("id") String id, @Body EditProfileRequest request);

    @Multipart
    @POST("/productServices/upload/image")
    Call<ImageResponse> postImage(@Part MultipartBody.Part image);


}
