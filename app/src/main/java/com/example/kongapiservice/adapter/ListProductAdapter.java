package com.example.kongapiservice.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kongapiservice.ProductDetailActivity;
import com.example.kongapiservice.R;
import com.example.kongapiservice.model.ItemListProduct;

import java.util.List;

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.ListProductViewHolder> {
    Context mContext;
    List<ItemListProduct> mList;


    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<ItemListProduct> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_product, parent, false);
        return new ListProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListProductAdapter.ListProductViewHolder holder, int position) {
        ItemListProduct itemListProduct = mList.get(position);

        if (itemListProduct == null) {
            return;
        }
        holder.tvName.setText(itemListProduct.getName());
        holder.tvDescription.setText(itemListProduct.getDescription());
        holder.clList.setOnClickListener(view -> {
            mContext.startActivity(new Intent(mContext, ProductDetailActivity.class));

        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ListProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvDescription;
        ConstraintLayout clList;


        public ListProductViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.name);
            tvDescription = itemView.findViewById(R.id.des);
            clList = itemView.findViewById(R.id.clItemList);

//            clList.setOnClickListener(view -> {
//                mContext.startActivity(new Intent(mContext, ProductDetailActivity.class));
//            });
        }
    }
}
