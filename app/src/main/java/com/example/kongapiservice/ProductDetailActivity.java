package com.example.kongapiservice;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kongapiservice.adapter.ListProductAdapter;
import com.example.kongapiservice.network.ApiService;
import com.example.kongapiservice.network.reponse.CategoryListResponse;
import com.example.kongapiservice.network.reponse.ImageResponse;
import com.example.kongapiservice.network.request.NewCategoryRequest;
import com.example.kongapiservice.network.request.NewProductRequest;
import com.example.kongapiservice.ui.Constant;

import java.io.FileNotFoundException;
import java.io.InputStream;

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
    String idCategory;

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


        getApiListProduct();

    }

    private void getApiListProduct() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idCategory = extras.getString(Constant.ID_CATEGORY);
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
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setTitle("Thêm mới sản phẩm");
//            alertDialog.setMessage("Hãy điền đầy đủ thông tin");
            LayoutInflater inflater = this.getLayoutInflater();
            View add_menu_layout = inflater.inflate(R.layout.dialog_add_product, null);
            alertDialog.setView(add_menu_layout);
            Button btnOpenGallery = add_menu_layout.findViewById(R.id.btnSelect);
            imgCategory = add_menu_layout.findViewById(R.id.imgCatgory);
            EditText edtNameCategory = add_menu_layout.findViewById(R.id.edt_insert_Name);
            EditText edtPrice = add_menu_layout.findViewById(R.id.edt_insert_Price);
            EditText edtDescription = add_menu_layout.findViewById(R.id.edt_insert_Description);


            btnOpenGallery.setOnClickListener(v -> {
                openGallery();
            });

            alertDialog.setPositiveButton("TẠO MỚI", (dialogInterface, i) -> {
                createCategoryApi(edtNameCategory.getText().toString(), Integer.parseInt(edtPrice.getText().toString()),
                        edtDescription.getText().toString(), idCategory);
                dialogInterface.dismiss();
            });
            alertDialog.setNegativeButton("HUỶ", (dialogInterface, i) -> dialogInterface.dismiss());
            alertDialog.show();

        }

    }

    private void createCategoryApi(String nameCategory, int price, String description, String categoryId) {
        addNewProduct(nameCategory, price, description,"https://i.ibb.co/7zw5gFH/977ba33de48d.png", categoryId);

//        filePath = RealPathUtil.getRealPath(this, selectedImage);
//
//        File file = new File(filePath);
//
//        RequestBody requestFile =
//                null;
//
//        requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("image", file.getName(), requestFile);
//
//
//        Call<ImageResponse> call = ApiService.apiServiceUpload.postImage(body);
//        call.enqueue(new Callback<ImageResponse>() {
//            @Override
//            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
//                if (response.body() != null && !Objects.equals(nameCategory, "")) {
//                } else {
//                    Log.d("AAA", " fail up anh----");
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ImageResponse> call, Throwable t) {
//                Log.d("AAA", t.getLocalizedMessage() + " throw----");
//
//            }
//        });
    }

    private void addNewProduct(String nameProduct, int price, String description, String imgURl, String categoryId) {
        Call<CategoryListResponse> call = ApiService.apiService.postProduct(new NewProductRequest(nameProduct, price, description, imgURl, categoryId));
        call.enqueue(new Callback<CategoryListResponse>() {
            @Override
            public void onResponse(Call<CategoryListResponse> call, Response<CategoryListResponse> response) {
                if (response.body() != null) {
                    Toast.makeText(ProductDetailActivity.this, "THANH CONG PRODUCT", Toast.LENGTH_SHORT).show();
                    getApiListProduct();
                } else {
                    Log.d("AAA", " fail tao category----");
                }
            }

            @Override
            public void onFailure(Call<CategoryListResponse> call, Throwable t) {

            }
        });

    }


    private void uploadImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn Ảnh "), PICK_IMAGE_REQUEST);

    }


    @Override
    public void sendName(String name, String idCategory, String urlImage) {
        openModify(name, idCategory, urlImage);
    }

    private void openModify(String nameItem, String idCategory, String urlImage) {
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            LayoutInflater inflater = this.getLayoutInflater();
            View add_menu_layout = inflater.inflate(R.layout.popup_edit, null);

            Button btnEdit = add_menu_layout.findViewById(R.id.btnEdit);
            Button btnRemove = add_menu_layout.findViewById(R.id.btnRemove);
            alertDialog.setView(add_menu_layout);

            Dialog dialog = alertDialog.create();

            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.55);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.3);

            dialog.show();
            dialog.getWindow().setLayout(width, height);
            btnEdit.setOnClickListener(view -> {
                openDialogEdit(nameItem, urlImage, idCategory);
                dialog.dismiss();
            });
            btnRemove.setOnClickListener(view -> {
                openDialogRemoveItem(nameItem, idCategory);
                dialog.dismiss();
            });
        }
    }


    private void openDialogAddCategory() {
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setTitle("Thêm mới sản phẩm");
//            alertDialog.setMessage("Hãy điền đầy đủ thông tin");
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
            View add_menu_layout = inflater.inflate(R.layout.dialog_add_product, null);
            alertDialog.setView(add_menu_layout);

            EditText edtNameItem = add_menu_layout.findViewById(R.id.edt_insert_Name);
            EditText edtNamePrice = add_menu_layout.findViewById(R.id.edt_insert_Price);
            EditText edtNameDescription = add_menu_layout.findViewById(R.id.edt_insert_Description);
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
            edtNamePrice.setText("gia");
            edtNameDescription.setText("mo ta");
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

//        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
//        } else {
//            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
//            requestPermissions(permission, PICK_IMAGE_REQUEST);
//        }


    }

    private void openDialogRemoveItem(String name, String idProduct) {
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

                Call<ImageResponse> call = ApiService.apiService.removeProduct(idProduct);
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
            imgCategory.setImageBitmap(bitmap);

        }
    }
}