package com.example.kongapiservice.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.kongapiservice.adapter.ListProductAdapter;
import com.example.kongapiservice.databinding.FragmentHomeBinding;
import com.example.kongapiservice.model.ItemListProduct;
import com.example.kongapiservice.network.ApiService;
import com.example.kongapiservice.network.reponse.CategoryListResponse;
import com.example.kongapiservice.network.reponse.LogInResponse;
import com.example.kongapiservice.network.request.RegisterRequest;
import com.example.kongapiservice.register.RegisterActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ListProductAdapter adapter;

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

    public List<ItemListProduct> getListProduct() {
        List<ItemListProduct> items = new ArrayList<>();
        items.add(new ItemListProduct("Quan Ao", "Rat dep"));
        items.add(new ItemListProduct("Quan Ao1", "Rat dep1"));
        items.add(new ItemListProduct("Quan Ao2", "Rat dep2"));
        items.add(new ItemListProduct("Quan Ao3", "Rat dep3"));
        items.add(new ItemListProduct("Quan Ao4", "Rat dep4"));
        return items;
    }
}