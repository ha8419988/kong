package com.example.kongapiservice.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.ListProductViewHolder> {
    @NonNull
    @Override
    public ListProductAdapter.ListProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ListProductAdapter.ListProductViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ListProductViewHolder extends RecyclerView.ViewHolder {
        public ListProductViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
