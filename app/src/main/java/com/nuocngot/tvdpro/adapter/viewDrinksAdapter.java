package com.nuocngot.tvdpro.adapter;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nuocngot.tvdpro.R;

import java.util.ArrayList;

public class viewDrinksAdapter extends RecyclerView.Adapter<viewDrinksAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<productAdapter> dataList;

    public viewDrinksAdapter(Context context, ArrayList<productAdapter> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }

    public viewDrinksAdapter(ArrayList<productAdapter> productDataList) {
        this.dataList = productDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drinks, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.proName.setText(dataList.get(position).getItemName());
        holder.proCost.setText(dataList.get(position).getItemPrice() + " VNĐ");
        String imageUrl = dataList.get(position).getItemImage();

        Glide.with(mContext)
                .load(imageUrl) // URL của hình ảnh
                .placeholder(R.drawable.img_avatar_nam)
                .into(holder.proImage);
        holder.proSL.setText("Kho: " + (dataList.get(position).getItemQuantity()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView proName, proCost, proSL;
        ImageView proImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            proName = itemView.findViewById(R.id.productName);
            proCost = itemView.findViewById(R.id.productCost);
            proSL = itemView.findViewById(R.id.productSL);
            proImage = itemView.findViewById(R.id.imgPro);
        }
    }
}

