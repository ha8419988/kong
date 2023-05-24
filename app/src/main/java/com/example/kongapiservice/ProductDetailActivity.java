package com.example.kongapiservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kongapiservice.adapter.ListProductAdapter;
import com.example.kongapiservice.network.ApiService;
import com.example.kongapiservice.network.reponse.CategoryListResponse;
import com.example.kongapiservice.ui.Constant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    private ListProductAdapter adapter;
    RecyclerView rcvCategory;
    ImageView imgEmpty;
    ImageView imgBack;
    TextView tvNameCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        rcvCategory = findViewById(R.id.recycle_category);
        imgEmpty = findViewById(R.id.imgEmpty);
        imgBack = findViewById(R.id.imgBack);
        tvNameCategory = findViewById(R.id.tvNameCategory);


        imgBack.setOnClickListener(view -> finish());

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
                    adapter = new ListProductAdapter();
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
                    Log.d("AAA", t.getLocalizedMessage());

                }
            });
        }

    }
}