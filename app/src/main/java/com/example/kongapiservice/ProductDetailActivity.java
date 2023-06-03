package com.example.kongapiservice;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kongapiservice.adapter.ListProductAdapter;
import com.example.kongapiservice.network.ApiService;
import com.example.kongapiservice.network.reponse.CategoryListResponse;
import com.example.kongapiservice.network.reponse.ImageResponse;
import com.example.kongapiservice.network.request.NewCategoryRequest;
import com.example.kongapiservice.ui.Constant;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.Permission;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity implements ListProductAdapter.sendNameCategory {
    private ListProductAdapter adapter;
    RecyclerView rcvCategory;
    ImageView imgEmpty;
    ImageView imgBack;
    ImageView imgAddProduct;
    TextView tvNameCategory;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri saveUri;
    private String filePath;
    private InputStream imageStream;
    private Bitmap bitmap;
    ImageView imgCategory;
    private Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        rcvCategory = findViewById(R.id.recycle_category);
        imgEmpty = findViewById(R.id.imgEmpty);
        imgBack = findViewById(R.id.imgBack);
        tvNameCategory = findViewById(R.id.tvNameCategory);
        imgAddProduct = findViewById(R.id.fab);
        adapter = new ListProductAdapter(ProductDetailActivity.this, this);


        imgBack.setOnClickListener(view -> {
            finish();
        });
        imgAddProduct.setOnClickListener(v -> {
            addProduct();
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

    private void addProduct() {
    }

    private void uploadImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn Ảnh "), PICK_IMAGE_REQUEST);

    }


    @Override
    public void sendName(String name, String idCategory, String urlImage) {

    }

    
    private void openDialogAddCategory() {
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setTitle("Thêm mới sản phẩm");
            alertDialog.setMessage("Hãy điền đầy đủ thông tin");
            LayoutInflater inflater = this.getLayoutInflater();
            View add_menu_layout = inflater.inflate(R.layout.dialog_add_product, null);
            alertDialog.setView(add_menu_layout);
            Button btnOpenGallery = add_menu_layout.findViewById(R.id.btnSelect);
//            imgCategory = add_menu_layout.findViewById(R.id.imgCatgory);
            EditText edtNameCategory = add_menu_layout.findViewById(R.id.edt_insert_Name);
            EditText edtPrice = add_menu_layout.findViewById(R.id.edt_insert_Price);
            EditText edtDescription = add_menu_layout.findViewById(R.id.edt_insert_Description);


            btnOpenGallery.setOnClickListener(v -> {
                openGallery();
            });

            alertDialog.setPositiveButton("TẠO MỚI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
//                    createCategoryApi(edtNameCategory.getText().toString());
                    dialogInterface.dismiss();
                }
            });
            alertDialog.setNegativeButton("HUỶ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialog.show();

        }
    }

    private void openDialogEdit(String name, String urlImage, String idCategory) {
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setTitle("Sửa mục thời trang");
            LayoutInflater inflater = this.getLayoutInflater();
            View add_menu_layout = inflater.inflate(R.layout.add_new_menu, null);
            alertDialog.setView(add_menu_layout);

            EditText edtNameItem = add_menu_layout.findViewById(R.id.edt_insert_Name);
            ImageView imgCategory = add_menu_layout.findViewById(R.id.imgCatgory);
            Button btnGallery = add_menu_layout.findViewById(R.id.btnSelect);
            btnGallery.setOnClickListener(v -> openGallery());

//
//            if (bitmap != null) {
//                imgCategory.setImageBitmap(bitmap);
//            }else {
//                Glide.with(getContext()).load(urlImage)
//                        .into(imgCategory);
//            }

            edtNameItem.setText(name);
            alertDialog.setPositiveButton("CẬP NHẬP", (dialogInterface, i) -> {
                Call<ImageResponse> call = ApiService.apiService.updateCategory(idCategory,
                        new NewCategoryRequest(edtNameItem.getText().toString(), urlImage));
                dialogInterface.dismiss();
            });
            alertDialog.setNegativeButton("HUỶ", (dialogInterface, i) -> dialogInterface.dismiss());
            alertDialog.show();

        }
    }

    private void openGallery() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, PICK_IMAGE_REQUEST);
        }


    }

    private void openDialogRemoveItem(String name, String idCategory) {
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            LayoutInflater inflater = this.getLayoutInflater();
            View add_menu_layout = inflater.inflate(R.layout.popup_remove_item, null);
            alertDialog.setView(add_menu_layout);

            TextView tvTitleRemove = add_menu_layout.findViewById(R.id.tvTitleRemove);
            TextView tvBtnCancel = add_menu_layout.findViewById(R.id.btnCancel);
            TextView tvBtnRemove = add_menu_layout.findViewById(R.id.btnRemove);
            tvTitleRemove.setText("Bạn có chắc xoá " + name + "không");
            Dialog dialog = alertDialog.create();
            tvBtnCancel.setOnClickListener(v -> dialog.dismiss());
            tvBtnRemove.setOnClickListener(v -> {
                dialog.dismiss();

                Call<ImageResponse> call = ApiService.apiService.removeCategory(idCategory);
                call.enqueue(new Callback<ImageResponse>() {
                    @Override
                    public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                        if (response.body() != null) {
//                            getApiList();
                            Log.d("aaa", "thanh cong");

                        }

                    }

                    @Override
                    public void onFailure(Call<ImageResponse> call, Throwable t) {
                        Log.d("aaa", t.getLocalizedMessage());
                    }
                });
            });
            dialog.show();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
//            imgCategory.setImageBitmap(bitmap);

        }
    }
}