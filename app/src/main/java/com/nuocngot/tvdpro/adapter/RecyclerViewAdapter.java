package com.nuocngot.tvdpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nuocngot.tvdpro.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<tabLayoutAdapter> dataList;
    private Context context;

    public RecyclerViewAdapter(ArrayList<tabLayoutAdapter> dataList) {
        this.dataList = dataList;
    }

    public RecyclerViewAdapter(Context context, ArrayList<productAdapter> productDataList) {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tablayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(dataList.get(position).getNameTab());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(position == 0) {
                   Toast.makeText(v.getContext(), "Home", Toast.LENGTH_SHORT).show();
               }
               else if(position == 1) {
                   Toast.makeText(v.getContext(), "Search", Toast.LENGTH_SHORT).show();
               }
               else if(position == 2) {
                   Toast.makeText(v.getContext(), "Favorite", Toast.LENGTH_SHORT).show();
               }
               else if(position == 3) {
                   Toast.makeText(v.getContext(), "Profile", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvtabTitle);
        }
    }
}
