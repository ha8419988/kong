package com.example.kongapiservice.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kongapiservice.ProductDetailActivity;
import com.example.kongapiservice.R;
import com.example.kongapiservice.network.reponse.CategoryListResponse;
import com.example.kongapiservice.ui.Constant;

import java.util.List;

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.ListProductViewHolder> {

    Context mContext;

    public ListProductAdapter(Context mContext, ListProductAdapter.sendNameCategory sendNameCategory) {
        this.mContext = mContext;
        this.sendNameCategory = sendNameCategory;
    }

    List<CategoryListResponse> mList;
    List<CategoryListResponse.Product> mListProduct;
    sendNameCategory sendNameCategory;


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
        String urlImage = "";
        String idCategory = "";
        int itemPrice = 0;
        String itemDescription = "";
        String idProduct = "";
        if (mListProduct != null) {
            holder.tvPrice.setVisibility(View.VISIBLE);
            holder.tvDescription.setVisibility(View.VISIBLE);

            CategoryListResponse.Product itemProduct = mListProduct.get(position);
            itemName = itemProduct.getName();
            urlImage = itemProduct.getImageURL();
            idProduct = itemProduct.getId();
            itemPrice = itemProduct.getPrices();
            itemDescription = itemProduct.getDescription();

        } else if (mList != null) {
            holder.tvPrice.setVisibility(View.GONE);
            holder.tvDescription.setVisibility(View.GONE);
            CategoryListResponse itemListProduct = mList.get(position);
            itemName = itemListProduct.getName();
            urlImage = itemListProduct.getImageURL();
            idCategory = itemListProduct.getId();

            holder.clList.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra(Constant.ID_CATEGORY, itemListProduct.getId());
                intent.putExtra(Constant.NAME_CATEGORY, itemListProduct.getName());
                mContext.startActivity(intent);
            });
        }

        String finalItemName = itemName;
        String finalIdCategory = idCategory;
        String finalurlImage = urlImage;
        String finalIdProduct = idProduct;
        int finalPrices = itemPrice;
        String finalDescription = itemDescription;

        holder.clList.setOnLongClickListener(view -> {
            sendNameCategory.sendName(finalItemName, finalIdCategory, finalurlImage, finalIdProduct,
                    finalPrices, finalDescription);
            return true;
        });
        if (mListProduct != null) {
            holder.tvPrice.setText(String.valueOf(itemPrice));
            holder.tvDescription.setText(itemDescription);

        }
        if (urlImage != null) {
            Glide.with(mContext)
                    .load(urlImage)
                    .into(holder.imgItem);
        }
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
        TextView tvPrice;
        ConstraintLayout clList;
        ImageView imgItem;


        public ListProductViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.name);
            tvDescription = itemView.findViewById(R.id.des);
            tvPrice = itemView.findViewById(R.id.price);
            clList = itemView.findViewById(R.id.clItemList);
            imgItem = itemView.findViewById(R.id.imgItem);
//
//            clList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//                @Override
//                public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//
//                }
//            });

        }


    }

    public interface sendNameCategory {
        void sendName(String name, String idCategory, String imgUrl, String idProduct
                , int Price, String description);

    }
}
