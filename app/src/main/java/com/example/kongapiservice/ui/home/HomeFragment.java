package com.example.kongapiservice.ui.home;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.kongapiservice.R;
import com.example.kongapiservice.adapter.ListProductAdapter;
import com.example.kongapiservice.databinding.FragmentHomeBinding;
import com.example.kongapiservice.network.ApiService;
import com.example.kongapiservice.network.reponse.CategoryListResponse;
import com.example.kongapiservice.ui.Constant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements ListProductAdapter.sendNameCategory {
    Button btn_select, btn_upload;
    EditText edt_insert_name;
    private FragmentHomeBinding binding;
    private ListProductAdapter adapter;
    private CategoryListResponse response;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final TextView textView = binding.textHome;
        adapter = new ListProductAdapter(getContext(), this);
        getApiList();


        return root;
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

    private void openModify(String nameItem) {
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

//
//            alertDialog.setTitle("Sửa mục thời trang");
//            alertDialog.setMessage("Hãy điền đầy đủ thông tin");

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
                openDialogEdit(nameItem);
                dialog.dismiss();
            });
        }
    }

    private void openDialogEdit(String name) {
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());


            alertDialog.setTitle("Sửa mục thời trang");
            alertDialog.setMessage("Hãy điền đầy đủ thông tin");
            LayoutInflater inflater = this.getLayoutInflater();
            View add_menu_layout = inflater.inflate(R.layout.add_new_menu, null);
            alertDialog.setView(add_menu_layout);

            EditText edtNameItem = add_menu_layout.findViewById(R.id.edt_insert_Name);
            edtNameItem.setText(name);

            alertDialog.show();

        }
    }


    @Override
    public void sendName(String name) {
        openModify(name);
    }
}