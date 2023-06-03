package com.example.kongapiservice.ui.home;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.kongapiservice.R;
import com.example.kongapiservice.RealPathUtil;
import com.example.kongapiservice.adapter.ListProductAdapter;
import com.example.kongapiservice.databinding.FragmentHomeBinding;
import com.example.kongapiservice.network.ApiService;
import com.example.kongapiservice.network.reponse.CategoryListResponse;
import com.example.kongapiservice.network.reponse.ImageResponse;
import com.example.kongapiservice.network.request.NewCategoryRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements ListProductAdapter.sendNameCategory {
    Button btn_select, btn_upload;
    EditText edt_insert_name;
    private FragmentHomeBinding binding;
    private ListProductAdapter adapter;
    private CategoryListResponse response;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri selectedImage;
    private String filePath;
    private InputStream imageStream;
    private Bitmap bitmap;
    ImageView imgCategory;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        adapter = new ListProductAdapter(getContext(), this);
        getApiList();
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogAddCategory();
            }
        });

        return root;
    }

    private void openGallery() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, PICK_IMAGE_REQUEST);
        }


    }

    private void createCategoryApi(String nameCategory) {
        filePath = RealPathUtil.getRealPath(getContext(), selectedImage);

        File file = new File(filePath);

        RequestBody requestFile =
                null;

        requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);


        Call<ImageResponse> call = ApiService.apiServiceUpload.postImage(body);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (response.body() != null && !Objects.equals(nameCategory, "")) {
                    addNewCategory(nameCategory, response.body().getData().getUrl());
                } else {
                    Log.d("AAA", " fail up anh----");

                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Log.d("AAA", t.getLocalizedMessage() + " throw----");

            }
        });
    }

    private void addNewCategory(String nameCategory, String imgURl) {
        Call<CategoryListResponse> call = ApiService.apiService.postCategory(new NewCategoryRequest(nameCategory, imgURl));
        call.enqueue(new Callback<CategoryListResponse>() {
            @Override
            public void onResponse(Call<CategoryListResponse> call, Response<CategoryListResponse> response) {
                if (response.body() != null) {
                    Toast.makeText(getContext(), "THANH CONG", Toast.LENGTH_SHORT).show();
                    getApiList();
                } else {
                    Log.d("AAA", " fail tao category----");
                }
            }

            @Override
            public void onFailure(Call<CategoryListResponse> call, Throwable t) {

            }
        });

    }


    private void getApiList() {
        Call<List<CategoryListResponse>> call = ApiService.apiService.getCategoryList();
        call.enqueue(new Callback<List<CategoryListResponse>>() {
            @Override
            public void onResponse(Call<List<CategoryListResponse>> call, Response<List<CategoryListResponse>> response) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

                binding.recycleMenu.setLayoutManager(gridLayoutManager);
                binding.recycleMenu.setAdapter(adapter);
                adapter.setData(response.body());
                adapter.setmContext(getContext());


            }

            @Override
            public void onFailure(Call<List<CategoryListResponse>> call, Throwable t) {

            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void openModify(String nameItem, String idCategory, String urlImage) {
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

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
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

            alertDialog.setTitle("Thêm mới sản phẩm");
            alertDialog.setMessage("Hãy điền đầy đủ thông tin");
            LayoutInflater inflater = this.getLayoutInflater();
            View add_menu_layout = inflater.inflate(R.layout.dialog_add_category, null);
            alertDialog.setView(add_menu_layout);
            Button btnOpenGallery = add_menu_layout.findViewById(R.id.btnSelect);
            imgCategory = add_menu_layout.findViewById(R.id.imgCatgory);
            EditText edtNameCategory = add_menu_layout.findViewById(R.id.edt_insert_Name);


            btnOpenGallery.setOnClickListener(v -> {
                openGallery();
            });

            alertDialog.setPositiveButton("TẠO MỚI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    createCategoryApi(edtNameCategory.getText().toString());
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
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

            alertDialog.setTitle("Sửa mục thời trang");
            LayoutInflater inflater = this.getLayoutInflater();
            View add_menu_layout = inflater.inflate(R.layout.add_new_menu, null);
            alertDialog.setView(add_menu_layout);

            EditText edtNameItem = add_menu_layout.findViewById(R.id.edt_insert_Name);
            ImageView imgCategory = add_menu_layout.findViewById(R.id.imgCatgory);
            Button btnGallery = add_menu_layout.findViewById(R.id.btnSelect);
            btnGallery.setOnClickListener(v -> openGallery());


            if (bitmap != null) {
                imgCategory.setImageBitmap(bitmap);
            }else {
                Glide.with(getContext()).load(urlImage)
                        .into(imgCategory);
            }

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

    private void openDialogRemoveItem(String name, String idCategory) {
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

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
                            getApiList();
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
    public void sendName(String name, String idCategory, String urlImage) {
        openModify(name, idCategory, urlImage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            selectedImage = data.getData();
            final Uri imageUri = data.getData();
            try {
                imageStream = getContext().getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            bitmap = BitmapFactory.decodeStream(imageStream);
//            imgCategory.setImageBitmap(bitmap);

        }
    }
}