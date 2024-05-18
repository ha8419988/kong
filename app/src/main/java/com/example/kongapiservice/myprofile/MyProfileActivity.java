package com.example.kongapiservice.myprofile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kongapiservice.R;
import com.example.kongapiservice.RealPathUtil;
import com.example.kongapiservice.network.ApiService;
import com.example.kongapiservice.network.reponse.CategoryListResponse;
import com.example.kongapiservice.network.reponse.ImageResponse;
import com.example.kongapiservice.network.reponse.MyProfileResponse;
import com.example.kongapiservice.network.request.EditProfileRequest;
import com.example.kongapiservice.network.request.NewProductRequest;
import com.example.kongapiservice.ui.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends AppCompatActivity {

    ImageView imgBack, avatar;
    EditText edtName, edtMail;
    Button btnUpdate;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri saveUri;
    private String filePath;
    private InputStream imageStream;
    private Bitmap bitmap;
    ImageView imgCategory;
    private Uri selectedImage;
    String idCategory;
    private final int PICK_IMAGE_EDIT_REQUEST = 72;
    private Uri selectEditImage;
    ProgressBar progressBar;
    ImageView imgCategoryEdit;

    private Bitmap bitmapEdit;
    String id, imageUrl;

    ImageView  imgAvatar;
    TextView tvName, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        SharedPreferences prefs = getSharedPreferences(Constant.SHARED_PREFERENCES, MODE_PRIVATE);
        id = prefs.getString(Constant.ID_USER, "");
        findViewSID();
        getApiProfile();

    }

    private void getApiProfile() {
        //"No name defined" is the default value.
//        progressBar.setVisibility(View.VISIBLE
//        );
        Call<MyProfileResponse> call = ApiService.apiService.getProfile(id);
        call.enqueue(new Callback<MyProfileResponse>() {
            @Override
            public void onResponse(Call<MyProfileResponse> call, Response<MyProfileResponse> response) {
                progressBar.setVisibility(View.GONE
                );
                edtName.setText(response.body().getName());
                edtMail.setText(response.body().getEmail());
                imageUrl = response.body().getAvatarUrl();
                Glide.with(MyProfileActivity.this).load(imageUrl).into(avatar);

            }

            @Override
            public void onFailure(Call<MyProfileResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE
                );
            }
        });
    }

    private void findViewSID() {
        imgBack = findViewById(R.id.imgBack);

        avatar = findViewById(R.id.imgAvatar);
        edtName = findViewById(R.id.edtName);
        edtMail = findViewById(R.id.edtEmail);
        btnUpdate = findViewById(R.id.btnUpdateProfile);




        tvName = findViewById(R.id.name);
        tvEmail = findViewById(R.id.email);
        imgBack.setOnClickListener(view -> finish());

        btnUpdate.setOnClickListener(v -> {
            if (bitmap != null) {
                File file = null;
                filePath = RealPathUtil.getRealPath(this, selectedImage);
                if (filePath != null) {
                    file = new File(filePath);
                }
                RequestBody requestFile = null;
                requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("image", file.getName(), requestFile);


                Call<ImageResponse> call = ApiService.apiServiceUpload.postImage(body);
                call.enqueue(new Callback<ImageResponse>() {
                    @Override
                    public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                        if (response.body() != null) {
                            Call<CategoryListResponse> callApi = ApiService.apiService.editProfile(id,
                                    new EditProfileRequest(edtName.getText().toString(), response.body().getData().getUrl()
                                    ));

                            callApi.enqueue(new Callback<CategoryListResponse>() {
                                @Override
                                public void onResponse(Call<CategoryListResponse> call, Response<CategoryListResponse> response) {
                                    getApiProfile();
                                    Log.d("AAA", " update Profile thanh cong----");

                                }

                                @Override
                                public void onFailure(Call<CategoryListResponse> call, Throwable t) {
                                    Log.d("AAA", " update Profile loi----");
                                }
                            });
                        } else {
                            Log.d("AAA", " fail up anh Profile----");

                        }
                    }

                    @Override
                    public void onFailure(Call<ImageResponse> call, Throwable t) {
                        Log.d("AAA", t.getLocalizedMessage() + " throw----");

                    }
                });
            } else {
                Call<CategoryListResponse> callApi = ApiService.apiService.editProfile(id,
                        new EditProfileRequest(edtName.getText().toString(), imageUrl
                        ));

                callApi.enqueue(new Callback<CategoryListResponse>() {
                    @Override
                    public void onResponse(Call<CategoryListResponse> call, Response<CategoryListResponse> response) {
                        getApiProfile();

                        Log.d("AAA", " update thanh cong1----");

                    }

                    @Override
                    public void onFailure(Call<CategoryListResponse> call, Throwable t) {
                        Log.d("AAA", " update loi----");
                    }
                });
            }
        });
        avatar.setOnClickListener(v -> {
            openGallery(PICK_IMAGE_REQUEST);
        });
    }

    private void openGallery(int pickImageRequest) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, pickImageRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            selectedImage = data.getData();
            final Uri imageUri = data.getData();
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            bitmap = BitmapFactory.decodeStream(imageStream);
            avatar.setImageBitmap(bitmap);

        }
        if (requestCode == PICK_IMAGE_EDIT_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            selectEditImage = data.getData();
            final Uri imageUri = data.getData();
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            bitmapEdit = BitmapFactory.decodeStream(imageStream);
            imgCategoryEdit.setImageBitmap(bitmapEdit);

        }
    }
}