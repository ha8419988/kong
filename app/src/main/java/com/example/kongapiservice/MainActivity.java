package com.example.kongapiservice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.HashMap;

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
    private Uri selectedImage;
    private String filePath;
    private InputStream imageStream;

    private MultipartBody.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
        binding.appBarMain.upLoad.setOnClickListener(view -> {
            upLoad();
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

    private void upLoad() {
        filePath = RealPathUtil.getRealPath(this, selectedImage);

        File file = new File(filePath);
        Bitmap bmp = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, bos);

        builder.addFormDataPart("file", file.getName(), RequestBody.create(MultipartBody.FORM, bos.toByteArray()));
        RequestBody requestBody = builder.build();


        RequestBody requestFile =
                null;

        requestFile = RequestBody.create( MediaType.parse("image/*"),file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", "file", requestFile);


        Call<ImageResponse> call = ApiService.apiService.postImage(requestBody);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (response.body() != null) {
                    Log.d("AAA", "ok");
                    Log.d("AAA", String.valueOf(call.request()));

                } else {
                    Log.d("AAA", "not ok");

                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Log.d("AAA", t.getLocalizedMessage() + " throw----");

            }
        });
//        ApiService.apiService.postImage(body).subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ImageResponse>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@NonNull ImageResponse imageResponse) {
//                        Toast.makeText(MainActivity.this, imageResponse.getData().getUrl(), Toast.LENGTH_SHORT).show();
//
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        Log.d("AAA", e.getLocalizedMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

    private void uploadImage() {
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
            selectedImage = data.getData();
            //            Uri selectedImage = data.getData();
            final Uri imageUri = data.getData();
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

            binding.appBarMain.imgAvatar.setImageBitmap(selectedImage);


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

    public String copyFileToInternal(Context context, Uri fileUri) {
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

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        Log.d("aaa", byteBuff.toByteArray().toString());
        return byteBuff.toByteArray();
    }

}