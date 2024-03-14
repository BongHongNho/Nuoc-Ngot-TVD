package com.nuocngot.tvdpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nuocngot.tvdpro.R;

import java.util.ArrayList;

public class viewDrinksAdapter extends RecyclerView.Adapter<viewDrinksAdapter.ViewHolder> {

    private ArrayList<productAdapter> dataList;
    private Context context;

    public viewDrinksAdapter(ArrayList<productAdapter> dataList) {
        this.dataList = dataList;
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
        holder.proCost.setText(String.valueOf(dataList.get(position).getItemPrice()));
        holder.proImage.setImageResource(dataList.get(position).getItemImage());
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView proName, proCost;
        ImageView proImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            proName = itemView.findViewById(R.id.productName);
            proCost = itemView.findViewById(R.id.productCost);
            proImage = itemView.findViewById(R.id.imgPro);
        }
    }
}
