package com.example.kongapiservice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kongapiservice.network.ApiService;
import com.example.kongapiservice.network.reponse.CategoryListResponse;
import com.example.kongapiservice.network.reponse.ImageResponse;
import com.example.kongapiservice.network.request.ImageRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kongapiservice.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri saveUri;
    private String filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void uploadImage() {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);

//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
////        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Chọn Ảnh "), PICK_IMAGE_REQUEST);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
                saveUri = data.getData();
    //            Uri selectedImage = data.getData();
                filePath =copyFileToInternal(this, saveUri);


                File file = new File(filePath);
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("image/*"), file);


    // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                ApiService.apiService.postImage(body).subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ImageResponse>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull ImageResponse imageResponse) {
                                Toast.makeText(MainActivity.this, imageResponse.getData().getUrl(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

    //            Call<ImageResponse> call = ApiService.apiService.postImage(body);
    //            call.enqueue(new Callback<ImageResponse>() {
    //                @Override
    //                public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
    //                    if (
    //                            response.body().status == 200
    //                    )   {
    //                        Toast.makeText(MainActivity.this, response.body().getData().getUrl(), Toast.LENGTH_SHORT).show();
    //
    //                    }
    //
    //                }
    //
    //                @Override
    //                public void onFailure(Call<ImageResponse> call, Throwable t) {
    //                    Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    //                    Log.d("AAAA", t.getLocalizedMessage());
    //                }
    //            });
        }
    }
//    public String getPath(Uri uri) {
//
////        String[] projection = { MediaStore.Images.Media.DATA };
////        Cursor cursor = managedQuery(uri, projection, null, null, null);
////        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
////        cursor.moveToFirst();
////
////        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
////        String picturePath = cursor.getString(columnIndex);
////        cursor.close();
////
////
////        return cursor.getString(column_index);
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public  String copyFileToInternal(Context context, Uri fileUri) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Cursor cursor = context.getContentResolver().query(fileUri, new String[]{OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE}, null, null);
            cursor.moveToFirst();

            @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

            File file = new File(context.getFilesDir() + "/" + displayName);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
                byte buffers[] = new byte[1024];
                int read;
                while ((read = inputStream.read(buffers)) != -1) {
                    fileOutputStream.write(buffers, 0, read);
                }
                inputStream.close();
                fileOutputStream.close();
                return file.getPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}