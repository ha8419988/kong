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
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kongapiservice.myprofile.MyProfileActivity;
import com.example.kongapiservice.network.ApiService;
import com.example.kongapiservice.network.reponse.ImageResponse;
import com.example.kongapiservice.ui.Constant;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri selectedImage;
    private String filePath;
    private InputStream imageStream;
    TextView tv_fullname, tvEmail;
    ImageView imgUser;
    LinearLayout lnUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();


        View headerView = navigationView.getHeaderView(0);//lay headerview o vitri nhat dinh
        tv_fullname = headerView.findViewById(R.id.nameUser);
        tvEmail = headerView.findViewById(R.id.tvEmailUser);
        lnUser = headerView.findViewById(R.id.navUser);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tv_fullname.setText("Xin chào : " + extras.getString(Constant.USER_NAME));
            tvEmail.setText(extras.getString(Constant.USER_NAME));
        }

//        tv_fullname.setText("Xin chào : " + extras.getString(Constant.USER_NAME));
//        tvEmail.setText(extras.getString(Constant.USER_NAME));
        lnUser.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, MyProfileActivity.class);
            startActivity(i);
        });

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_logout) {
            finish();
        }

        return true;
    }
}