package com.example.kongapiservice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kongapiservice.adapter.ListProductAdapter;
import com.example.kongapiservice.network.ApiService;
import com.example.kongapiservice.network.reponse.CategoryListResponse;
import com.example.kongapiservice.network.reponse.ImageResponse;
import com.example.kongapiservice.ui.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity implements ListProductAdapter.sendNameCategory {
    private ListProductAdapter adapter;
    RecyclerView rcvCategory;
    ImageView imgEmpty;
    ImageView imgBack;
    TextView tvNameCategory;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri saveUri;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        rcvCategory = findViewById(R.id.recycle_category);
        imgEmpty = findViewById(R.id.imgEmpty);
        imgBack = findViewById(R.id.imgBack);
        tvNameCategory = findViewById(R.id.tvNameCategory);
        adapter = new ListProductAdapter(ProductDetailActivity.this,this);


        imgBack.setOnClickListener(view -> {
           finish();
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String idCategory = extras.getString(Constant.ID_CATEGORY);
            String nameCategory = extras.getString(Constant.NAME_CATEGORY);
            tvNameCategory.setText(nameCategory);

            Call<CategoryListResponse> call = ApiService.apiService.getCategoryDetail(idCategory);
            call.enqueue(new Callback<CategoryListResponse>() {
                @Override
                public void onResponse(Call<CategoryListResponse> call, Response<CategoryListResponse> response) {
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(ProductDetailActivity.this, 2);
                    if (response.body().product.size() > 0) {
                        rcvCategory.setVisibility(View.VISIBLE);
                        imgEmpty.setVisibility(View.GONE);
                    } else {
                        rcvCategory.setVisibility(View.GONE);
                        imgEmpty.setVisibility(View.VISIBLE);

                    }
                    rcvCategory.setLayoutManager(gridLayoutManager);
                    rcvCategory.setAdapter(adapter);
                    adapter.setDataCategoryDetail(response.body().product);
                    adapter.setmContext(ProductDetailActivity.this);

                }

                @Override
                public void onFailure(Call<CategoryListResponse> call, Throwable t) {
                    Toast.makeText(ProductDetailActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

    private void uploadImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn Ảnh "), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void sendName(String name) {

    }
}