package com.nuocngot.tvdpro.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.activity.CategoryActivity;
import com.nuocngot.tvdpro.model.Category;
import com.nuocngot.tvdpro.model.SelectTab;

import java.util.ArrayList;

public class SelectTabAdapter extends RecyclerView.Adapter<SelectTabAdapter.ViewHolder> {
    private ArrayList<SelectTab> selectTabs;
    private ArrayList<Category> categoryList;
    private Context context;

    public SelectTabAdapter(Context context, ArrayList<SelectTab> selectTabs) {
        this.context = context;
        this.selectTabs = selectTabs;
    }

    @NonNull
    @Override
    public SelectTabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_tablayout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTabAdapter.ViewHolder holder, int position) {
        SelectTab selectTab = selectTabs.get(position);
        Glide.with(holder.itemView.getContext()).load(selectTab.getTabName()).into(holder.tabName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int maDM;
                int position = holder.getAdapterPosition();
                if(position == 0){
                    maDM = 1;
                }else if(position == 1){
                    maDM = 5;
                }else if(position == 2){
                    maDM = 2;
                }else{
                    maDM = 9;
                }
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("maDM", maDM);
                intent.putExtra("tenDM", selectTab.getTabName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectTabs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView tabName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tabName = itemView.findViewById(R.id.tvtabTitle);
        }
    }

}
