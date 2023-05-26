package com.example.kongapiservice.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class HomeFragment extends Fragment {
    Button btn_select, btn_upload;
    EditText edt_insert_name;
    private FragmentHomeBinding binding;
    private ListProductAdapter adapter;
    private CategoryListResponse response;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final TextView textView = binding.textHome;

        getApiList();


        return root;
    }

    private void getApiList() {
        Call<List<CategoryListResponse>> call = ApiService.apiService.getCategoryList();
        call.enqueue(new Callback<List<CategoryListResponse>>() {
            @Override
            public void onResponse(Call<List<CategoryListResponse>> call, Response<List<CategoryListResponse>> response) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                adapter = new ListProductAdapter();

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

    private void UpdateCategory() {
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle("Sửa mục thời trang");
            alertDialog.setMessage("Hãy điền đầy đủ thông tin");

            LayoutInflater inflater = this.getLayoutInflater();
            View add_menu_layout = inflater.inflate(R.layout.add_new_menu, null);

            edt_insert_name = add_menu_layout.findViewById(R.id.edt_insert_Name);
            btn_select = add_menu_layout.findViewById(R.id.btnSelect);
            btn_upload = add_menu_layout.findViewById(R.id.btnUpLoad);
//            btn_select.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    chooseImage();
//                }
//            });
//            btn_upload.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    changeImage(item);
//                }
//            });
            //set value tên
//            edt_insert_name.setText(item.getName());
            alertDialog.setView(add_menu_layout);
//            alertDialog.setIcon(R.drawable.shopping_cart1_24);

            alertDialog.setPositiveButton("CÓ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
//                    item.setName(edt_insert_name.getText().toString());
//                    category.child(key).setValue(item);

                }
            });
            alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();

        }
    }

    @Override
    public boolean onContextItemSelected(@androidx.annotation.NonNull MenuItem item) {
        if (item.getTitle().equals(Constant.UPDATE)) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

//            Object obj = getListView().getItemAtPosition(info.position);
//            String name = obj.toString();
//            UpdateCategory();
//            Log.d("AAA",item.getMenuInfo());
//            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Log.d("AAA", String.valueOf(info.id));

//            Toast.makeText(getContext(), "update", Toast.LENGTH_SHORT).show();
        } else if (item.getTitle().equals(Constant.DELETE)) {
//            deleteCategory(adapter.getRef(item.getOrder()).getKey());
            Toast.makeText(getContext(), "xoa", Toast.LENGTH_SHORT).show();

        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }
}