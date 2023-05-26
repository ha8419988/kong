package com.example.kongapiservice.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
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
import com.example.kongapiservice.network.reponse.CategoryListResponse;
import com.example.kongapiservice.ui.Constant;

import java.util.List;

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.ListProductViewHolder> {
    Context mContext;
    List<CategoryListResponse> mList;
    List<CategoryListResponse.Product> mListProduct;


    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<CategoryListResponse> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void setDataCategoryDetail(List<CategoryListResponse.Product> mList) {
        this.mListProduct = mList;
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
        String itemName = "";
        if (mListProduct != null) {
            CategoryListResponse.Product itemProduct = mListProduct.get(position);
            itemName = itemProduct.getName();
        } else if (mList != null) {
            CategoryListResponse itemListProduct = mList.get(position);
            itemName = itemListProduct.getName();
            holder.clList.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra(Constant.ID_CATEGORY, itemListProduct.getId());
                intent.putExtra(Constant.NAME_CATEGORY, itemListProduct.getName());
                mContext.startActivity(intent);
            });
        }
        holder.clList.setOnCreateContextMenuListener((contextMenu, view, contextMenuInfo) -> {
            contextMenu.setHeaderTitle("Chọn Hành Động");
                contextMenu.add(0, 0,position, Constant.UPDATE);
                contextMenu.add(0, 0, position, Constant.DELETE);
//                contextMenu.
        });

        holder.tvName.setText(itemName);

    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        else if (mListProduct != null
        ) {
            return mListProduct.size();
        }
        return 0;
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
//
//            clList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//                @Override
//                public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//
//                }
//            });

        }


    }
}
