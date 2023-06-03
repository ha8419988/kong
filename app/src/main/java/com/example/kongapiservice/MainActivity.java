package com.example.kongapiservice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.example.kongapiservice.network.ApiService;
import com.example.kongapiservice.network.reponse.ImageResponse;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kongapiservice.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri selectedImage;
    private String filePath;
    private InputStream imageStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void upLoad() {
        filePath = RealPathUtil.getRealPath(this, selectedImage);

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
                if (response.body() != null) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Log.d("AAA", t.getLocalizedMessage() + " throw----");

            }
        });
    }

    private void openGallery() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, PICK_IMAGE_REQUEST);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}